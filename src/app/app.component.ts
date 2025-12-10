import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common'; // Fixes *ngIf and *ngFor
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';   // Fixes [(ngModel)]

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule], // IMPORTANT: Modules must be here
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private http = inject(HttpClient);
  
  // State Variables (These were missing in your file!)
  messages: { text: string; isUser: boolean }[] = [];
  userMessage: string = '';
  isUploading: boolean = false;
  isThinking: boolean = false;
  uploadStatus: string = '';

  // 1. Upload Function
  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    this.isUploading = true;
    this.uploadStatus = 'Uploading & Ingesting...';

    const formData = new FormData();
    formData.append('file', file);

    this.http.post('http://localhost:8080/api/documents', formData).subscribe({
      next: (res: any) => {
        this.isUploading = false;
        this.uploadStatus = '✅ Document Processed! You can chat now.';
        this.messages.push({ text: `I have read "${res.filename}". Ask me anything about it!`, isUser: false });
      },
      error: (err) => {
        this.isUploading = false;
        this.uploadStatus = '❌ Error uploading file.';
        console.error(err);
      }
    });
  }

  // 2. Chat Function
  sendMessage() {
    if (!this.userMessage.trim()) return;

    // Add user message to UI
    const question = this.userMessage;
    this.messages.push({ text: question, isUser: true });
    this.userMessage = '';
    this.isThinking = true;

    // Call Backend
    this.http.post<any>(`http://localhost:8080/api/chat?question=${encodeURIComponent(question)}`, {})
      .subscribe({
        next: (res) => {
          this.messages.push({ text: res.answer, isUser: false });
          this.isThinking = false;
        },
        error: (err) => {
          this.messages.push({ text: "⚠️ Error connecting to AI.", isUser: false });
          this.isThinking = false;
        }
      });
  }
}