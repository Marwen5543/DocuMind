# 🧠 DocuMind - AI-Powered Document Intelligence Platform

![Build Status](https://github.com/Marwen5543/DocuMind/actions/workflows/backend-ci.yml/badge.svg) ![Frontend Status](https://github.com/Marwen5543/DocuMind/actions/workflows/frontend-ci.yml/badge.svg)

**DocuMind** is an enterprise-grade **RAG (Retrieval-Augmented Generation)** platform that allows users to chat with their PDF documents. It uses a split-architecture AI pipeline to ingest documents in seconds and answer questions with high context awareness.

## 🏗️ Architecture
This project implements a **Modern RAG Pipeline**:
1.  **Ingestion Layer:** Uses **Apache Tika** & **Nomic-Embed-Text** (Local) for high-speed vectorization.
2.  **Inference Layer:** Uses **Mistral 7B** via **Spring AI** for context-aware responses.
3.  **Frontend:** Angular 17 + TailwindCSS for a ChatGPT-like experience.
4.  **DevOps:** Automated CI Pipelines via GitHub Actions.

```mermaid
graph LR
    User[User Uploads PDF] --> API[Spring Boot API]
    API --> Tika[Apache Tika Extraction]
    Tika --> Splitter[Token Splitter]
    Splitter --> Nomic[Ollama: Nomic-Embed]
    Nomic --> VectorDB[(In-Memory Vector Store)]
    
    User2[User Asks Question] --> API
    API --> VectorDB
    VectorDB -- Returns Context --> Mistral[Ollama: Mistral]
    Mistral -- Generates Answer --> API
    API --> UI[Angular UI]

🛠️ Tech Stack
Backend
Core: Java 17, Spring Boot 3.4
AI: Spring AI, Ollama (Mistral + Nomic)
Data: In-Memory Vector Store, JPA
Testing: JUnit 5
Frontend
Framework: Angular 17 (Standalone Components)
Styling: TailwindCSS (Dark Mode)
Communication: REST API
DevOps
CI/CD: GitHub Actions (Maven & NPM Pipelines)
VCS: Git (Feature Branch Workflow)

🚀 How to Run
Prerequisites
Ollama installed with models: ollama run mistral and ollama pull nomic-embed-text
Java 17+ and Node.js 20+
1. Start the Backend
code

git checkout backend
mvn spring-boot:run
2. Start the Frontend
code
git checkout frontend
npm install
ng serve
