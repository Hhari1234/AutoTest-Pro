import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const repoRoot = path.resolve(__dirname, '..');
const xmlPath = path.join(repoRoot, 'target', 'surefire-reports', 'testng-results.xml');
const outputPath = path.join(repoRoot, 'dashboard', 'public', 'results.json');

function formatFallbackData() {
  return {
    summary: {
      total: 0,
      passed: 0,
      failed: 0,
      skipped: 0,
      durationMs: 0,
      suiteName: 'AutoTest Pro Suite',
      startedAt: null,
      finishedAt: null,
    },
    tests: [],
    classBreakdown: [],
    generatedAt: new Date().toISOString(),
    status: 'No test run data available.',
  };
}

function getAttribute(tag, attributeName) {
  const match = tag.match(new RegExp(`${attributeName}="([^"]+)"`, 'i'));
  return match?.[1] ?? null;
}

function parseDashboardData(xml) {
  const resultTag = xml.match(/<testng-results\b[^>]*>/i)?.[0] ?? '';
  const suiteTag = xml.match(/<suite\b[^>]*>/i)?.[0] ?? '';

  const summary = {
    total: Number(getAttribute(resultTag, 'total') ?? 0),
    passed: Number(getAttribute(resultTag, 'passed') ?? 0),
    failed: Number(getAttribute(resultTag, 'failed') ?? 0),
    skipped: Number(getAttribute(resultTag, 'skipped') ?? 0),
    durationMs: Number(getAttribute(suiteTag, 'duration-ms') ?? 0),
    suiteName: getAttribute(suiteTag, 'name') ?? 'AutoTest Pro Suite',
    startedAt: getAttribute(suiteTag, 'started-at') ?? null,
    finishedAt: getAttribute(suiteTag, 'finished-at') ?? null,
  };

  const methodPattern = /<test-method\b[^>]*>/gi;
  const tests = [];

  for (const match of xml.matchAll(methodPattern)) {
    const tag = match[0];
    if (/\bis-config="true"/i.test(tag)) continue;

    const name = (tag.match(/\bname="([^"]+)"/i) ?? [])[1] ?? 'UnnamedTest';
    const status = (tag.match(/\bstatus="([^"]+)"/i) ?? [])[1] ?? 'UNKNOWN';
    const durationMs = Number((tag.match(/\bduration-ms="(\d+)"/i) ?? [])[1] ?? 0);
    const signature = (tag.match(/\bsignature="([^"]+)"/i) ?? [])[1] ?? '';
    const description = (tag.match(/\bdescription="([^"]*)"/i) ?? [])[1] ?? name;

    const classMatch = signature.match(/instance:([A-Za-z0-9_.]+)@/i);
    const methodMatch = signature.match(/([A-Za-z0-9_]+)\(\)/);

    tests.push({
      name: methodMatch?.[1] ?? name,
      className: classMatch?.[1] ?? 'UnknownSuite',
      description: description || name,
      status: String(status).toUpperCase(),
      durationMs: Number(durationMs || 0),
    });
  }

  const classMap = new Map();
  for (const test of tests) {
    const current = classMap.get(test.className) ?? {
      className: test.className,
      total: 0,
      passed: 0,
      failed: 0,
      skipped: 0,
    };

    current.total += 1;
    if (test.status === 'PASS') current.passed += 1;
    else if (test.status === 'FAIL') current.failed += 1;
    else if (test.status === 'SKIP' || test.status === 'SKIPPED') current.skipped += 1;

    classMap.set(test.className, current);
  }

  return {
    summary,
    tests,
    classBreakdown: [...classMap.values()].sort((a, b) => b.total - a.total),
    generatedAt: new Date().toISOString(),
  };
}

if (!fs.existsSync(xmlPath)) {
  const fallback = formatFallbackData();
  fs.mkdirSync(path.dirname(outputPath), { recursive: true });
  fs.writeFileSync(outputPath, JSON.stringify(fallback, null, 2));
  console.log('No Maven test results were found. Wrote fallback dashboard payload.');
  process.exit(0);
}

const xml = fs.readFileSync(xmlPath, 'utf8');
const dashboardData = parseDashboardData(xml);
fs.mkdirSync(path.dirname(outputPath), { recursive: true });
fs.writeFileSync(outputPath, JSON.stringify(dashboardData, null, 2));
console.log(
  `Dashboard data generated for ${dashboardData.tests.length} tests across ${dashboardData.classBreakdown.length} classes.`,
);
