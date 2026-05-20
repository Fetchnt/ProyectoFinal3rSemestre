// src/app/runcode/runcode.ts
import { Component } from '@angular/core';

interface OutputLine {
  type: 'info' | 'output' | 'error' | 'system';
  text: string;
  timestamp: string;
}

@Component({
  standalone: false,
  selector: 'app-runcode',
  templateUrl: './runcode.html',
  styleUrls: ['./runcode.css']
})
export class Runcode {

  languages = [
    { id: 'python', name: 'Python 3.11', icon: '🐍' },
    { id: 'javascript', name: 'JavaScript (Node 20)', icon: '⚡' },
    { id: 'typescript', name: 'TypeScript 5', icon: '📘' },
    { id: 'java', name: 'Java 21', icon: '☕' },
    { id: 'cpp', name: 'C++ 17', icon: '⚙' },
    { id: 'go', name: 'Go 1.22', icon: '🔵' },
    { id: 'rust', name: 'Rust 1.78', icon: '🦀' },
    { id: 'php', name: 'PHP 8.3', icon: '🐘' },
    { id: 'ruby', name: 'Ruby 3.3', icon: '💎' }
  ];

  selectedLang = 'python';
  inputCode = '';
  outputLines: OutputLine[] = [];
  isRunning = false;
  runCount = 0;
  stdinInput = '';
  showStdin = false;

  get currentLang() {
    return this.languages.find(l => l.id === this.selectedLang);
  }

  private getTimestamp(): string {
    return new Date().toLocaleTimeString('es-CO', { hour12: false });
  }

  runCode(): void {
    if (!this.inputCode.trim() || this.isRunning) return;

    this.isRunning = true;
    this.runCount++;

    const runId = this.runCount;
    const lang = this.currentLang;

    this.outputLines.push({
      type: 'system',
      text: `▶ Ejecutando ${lang?.name} — Run #${runId} — ${this.getTimestamp()}`,
      timestamp: this.getTimestamp()
    });

    // Simulate execution
    setTimeout(() => {
      this.outputLines.push({
        type: 'output',
        text: this.getMockOutput(),
        timestamp: this.getTimestamp()
      });

      this.outputLines.push({
        type: 'info',
        text: `✓ Proceso terminado en ${(Math.random() * 0.8 + 0.1).toFixed(3)}s — Código de salida: 0`,
        timestamp: this.getTimestamp()
      });

      this.isRunning = false;
      this.scrollToBottom();
    }, 1200);
  }

  private getMockOutput(): string {
    return `[Conecta tu backend de ejecución para ver resultados reales]\nCódigo recibido: ${this.inputCode.split('\n').length} líneas en ${this.selectedLang}`;
  }

  clearOutput(): void {
    this.outputLines = [];
    this.runCount = 0;
  }

  copyOutput(): void {
    const text = this.outputLines.map(l => l.text).join('\n');
    navigator.clipboard.writeText(text);
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      const terminal = document.querySelector('.terminal-output');
      if (terminal) terminal.scrollTop = terminal.scrollHeight;
    }, 50);
  }

  getLineClass(type: string): string {
    const map: Record<string, string> = {
      system: 'line-system',
      output: 'line-output',
      error: 'line-error',
      info: 'line-info'
    };
    return map[type] || '';
  }
}
