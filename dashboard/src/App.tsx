import { useEffect, useMemo, useState } from 'react';
import {
  Activity,
  CheckCircle2,
  ChevronRight,
  Clock3,
  FileText,
  ShieldCheck,
  XCircle,
} from 'lucide-react';
import {
  Bar,
  BarChart,
  CartesianGrid,
  Cell,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts';
import './App.css';

type Status = 'PASS' | 'FAIL' | 'SKIP' | 'SKIPPED';

type TestCase = {
  name: string;
  className: string;
  description: string;
  status: Status;
  durationMs: number;
};

type DashboardData = {
  summary: {
    total: number;
    passed: number;
    failed: number;
    skipped: number;
    durationMs: number;
    suiteName: string;
    startedAt: string | null;
    finishedAt: string | null;
  };
  tests: TestCase[];
  classBreakdown: Array<{
    className: string;
    total: number;
    passed: number;
    failed: number;
    skipped: number;
  }>;
  generatedAt: string;
  status?: string;
};

function formatDuration(ms: number) {
  if (!ms) return '0s';
  if (ms >= 60000) return `${(ms / 60000).toFixed(1)} min`;
  return `${(ms / 1000).toFixed(1)}s`;
}

function formatTimestamp(value: string | null) {
  if (!value) return '—';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleString([], {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}

function App() {
  const [data, setData] = useState<DashboardData | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('/results.json')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Results file not found');
        }
        return response.json();
      })
      .then((payload) => setData(payload))
      .catch(() => setData({
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
      }))
      .finally(() => setLoading(false));
  }, []);

  const pieData = useMemo(() => {
    if (!data) return [];
    return [
      { name: 'Passed', value: data.summary.passed, color: '#22c55e' },
      { name: 'Failed', value: data.summary.failed, color: '#ef4444' },
      { name: 'Skipped', value: data.summary.skipped, color: '#f59e0b' },
    ].filter((entry) => entry.value > 0);
  }, [data]);

  const barData = useMemo(() => {
    if (!data) return [];
    return data.classBreakdown.map((entry) => ({
      name: entry.className.split('.').pop() ?? entry.className,
      total: entry.total,
      passed: entry.passed,
      failed: entry.failed,
      skipped: entry.skipped,
    }));
  }, [data]);

  const passRate = data && data.summary.total > 0 ? ((data.summary.passed / data.summary.total) * 100).toFixed(1) : '0.0';

  return (
    <div className="dashboard-shell">
      <header className="topbar">
        <div>
          <p className="eyebrow">QA automation portfolio</p>
          <h1>AutoTest Pro Results Dashboard</h1>
        </div>
        <div className="topbar-meta">
          <span className="status-chip">Live TestNG output</span>
          <span>{data ? formatTimestamp(data.generatedAt) : 'Waiting for data'}</span>
        </div>
      </header>

      {loading ? (
        <div className="status-panel">Loading results…</div>
      ) : data && data.status ? (
        <div className="status-panel warning">{data.status}</div>
      ) : (
        <>
          <section className="summary-grid">
            <article className="summary-card accent">
              <div className="summary-icon success">
                <CheckCircle2 size={20} />
              </div>
              <div>
                <span className="label">Total tests</span>
                <strong>{data?.summary.total ?? 0}</strong>
              </div>
            </article>

            <article className="summary-card">
              <div className="summary-icon success">
                <ShieldCheck size={20} />
              </div>
              <div>
                <span className="label">Pass rate</span>
                <strong>{passRate}%</strong>
              </div>
            </article>

            <article className="summary-card">
              <div className="summary-icon danger">
                <XCircle size={20} />
              </div>
              <div>
                <span className="label">Failures</span>
                <strong>{data?.summary.failed ?? 0}</strong>
              </div>
            </article>

            <article className="summary-card">
              <div className="summary-icon warning">
                <Clock3 size={20} />
              </div>
              <div>
                <span className="label">Duration</span>
                <strong>{formatDuration(data?.summary.durationMs ?? 0)}</strong>
              </div>
            </article>
          </section>

          <section className="panel-grid">
            <article className="panel chart-panel">
              <div className="panel-header">
                <div>
                  <p className="eyebrow">Coverage</p>
                  <h2>{data?.summary.suiteName ?? 'AutoTest Pro Suite'}</h2>
                </div>
                <span className="meta-pill">{data?.summary.passed ?? 0} passed</span>
              </div>

              <div className="chart-box">
                <ResponsiveContainer width="100%" height={260}>
                  <PieChart>
                    <Pie data={pieData} dataKey="value" nameKey="name" innerRadius={52} outerRadius={88} paddingAngle={3}>
                      {pieData.map((entry) => (
                        <Cell key={entry.name} fill={entry.color} />
                      ))}
                    </Pie>
                    <Tooltip
                      formatter={(value) => {
                        const numericValue = Array.isArray(value)
                          ? Number(value[0] ?? 0)
                          : Number(value ?? 0);
                        return [`${numericValue} tests`, 'Count'];
                      }}
                    />
                  </PieChart>
                </ResponsiveContainer>
              </div>
            </article>

            <article className="panel chart-panel">
              <div className="panel-header">
                <div>
                  <p className="eyebrow">Execution</p>
                  <h2>Class breakdown</h2>
                </div>
                <span className="meta-pill">{data?.classBreakdown.length ?? 0} classes</span>
              </div>

              <div className="chart-box small">
                <ResponsiveContainer width="100%" height={260}>
                  <BarChart data={barData}>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} />
                    <XAxis dataKey="name" tickLine={false} axisLine={false} />
                    <YAxis allowDecimals={false} tickLine={false} axisLine={false} />
                    <Tooltip />
                    <Bar dataKey="passed" fill="#22c55e" radius={[8, 8, 0, 0]} />
                    <Bar dataKey="failed" fill="#ef4444" radius={[8, 8, 0, 0]} />
                    <Bar dataKey="skipped" fill="#f59e0b" radius={[8, 8, 0, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </article>
          </section>

          <section className="panel">
            <div className="panel-header">
              <div>
                <p className="eyebrow">Traceability</p>
                <h2>Test case details</h2>
              </div>
              <span className="meta-pill">{data?.tests.length ?? 0} methods</span>
            </div>

            <div className="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Test</th>
                    <th>Suite</th>
                    <th>Status</th>
                    <th>Duration</th>
                  </tr>
                </thead>
                <tbody>
                  {(data?.tests ?? []).map((test) => (
                    <tr key={`${test.className}-${test.name}`}>
                      <td>
                        <div className="test-name-block">
                          <strong>{test.name}</strong>
                          <small>{test.description}</small>
                        </div>
                      </td>
                      <td>{test.className.split('.').pop() ?? test.className}</td>
                      <td>
                        <span className={`status-pill ${test.status.toLowerCase()}`}>
                          {test.status}
                        </span>
                      </td>
                      <td>{formatDuration(test.durationMs)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>

          <section className="info-grid">
            <article className="info-card">
              <FileText size={18} />
              <div>
                <span>Started</span>
                <strong>{formatTimestamp(data?.summary.startedAt ?? null)}</strong>
              </div>
            </article>

            <article className="info-card">
              <Activity size={18} />
              <div>
                <span>Finished</span>
                <strong>{formatTimestamp(data?.summary.finishedAt ?? null)}</strong>
              </div>
            </article>

            <article className="info-card">
              <ChevronRight size={18} />
              <div>
                <span>Suite</span>
                <strong>{data?.summary.suiteName ?? 'AutoTest Pro Suite'}</strong>
              </div>
            </article>
          </section>
        </>
      )}
    </div>
  );
}

export default App;
