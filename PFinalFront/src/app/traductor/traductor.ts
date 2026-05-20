// src/app/traductor/traductor.ts
import { Component } from '@angular/core';

@Component({
  standalone: false,
  selector: 'app-traductor',
  templateUrl: './traductor.html',
  styleUrls: ['./traductor.css']
})
export class Traductor {

  aiModels = [
    { id: 'gpt4', name: 'GPT-4o', provider: 'OpenAI' },
    { id: 'claude', name: 'Claude 3.5 Sonnet', provider: 'Anthropic' },
    { id: 'gemini', name: 'Gemini 1.5 Pro', provider: 'Google' },
    { id: 'llama', name: 'Llama 3.1 70B', provider: 'Meta' },
    { id: 'mistral', name: 'Mistral Large', provider: 'Mistral AI' },
    { id: 'deepseek', name: 'DeepSeek Coder V2', provider: 'DeepSeek' }
  ];

  languages = [
    'Python', 'JavaScript', 'TypeScript', 'Java', 'C',
    'C++', 'C#', 'Go', 'Rust', 'Swift', 'Kotlin',
    'PHP', 'Ruby', 'Scala', 'Dart', 'R', 'MATLAB',
    'Bash', 'PowerShell', 'SQL', 'HTML/CSS'
  ];

  selectedAI = '';
  sourceLang = '';
  targetLang = '';
  inputCode = '';
  outputCode = '';
  isTranslating = false;
  copied = false;
  errorMessage = '';

  get canTranslate(): boolean {
    return !!this.selectedAI && !!this.sourceLang && !!this.targetLang
      && !!this.inputCode.trim() && this.sourceLang !== this.targetLang;
  }

  translate(): void {
    if (!this.canTranslate) return;

    this.isTranslating = true;
    this.errorMessage = '';
    this.outputCode = '';

    // Simulate API call
    setTimeout(() => {
      this.outputCode = `// Translated from ${this.sourceLang} to ${this.targetLang}\n// Using: ${this.getAIName()}\n\n${this.generateMockOutput()}`;
      this.isTranslating = false;
    }, 1500);
  }

  private getAIName(): string {
    const ai = this.aiModels.find(a => a.id === this.selectedAI);
    return ai ? `${ai.name} (${ai.provider})` : '';
  }

  private generateMockOutput(): string {
    return `// [Aquí irá el código traducido a ${this.targetLang}]\n// Conecta tu API de IA para obtener traducciones reales`;
  }

  copyOutput(): void {
    if (!this.outputCode) return;
    navigator.clipboard.writeText(this.outputCode).then(() => {
      this.copied = true;
      setTimeout(() => this.copied = false, 2000);
    });
  }

  clearAll(): void {
    this.inputCode = '';
    this.outputCode = '';
    this.errorMessage = '';
  }

  swapLanguages(): void {
    const temp = this.sourceLang;
    this.sourceLang = this.targetLang;
    this.targetLang = temp;
    if (this.outputCode) {
      const tempCode = this.inputCode;
      this.inputCode = this.outputCode;
      this.outputCode = tempCode;
    }
  }
}
