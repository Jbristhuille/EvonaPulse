<p align="center">
  <img src="docs/imgs/icon.png" alt="EvonaPulse Logo" width="200"/>
</p>

# EvonaPulse

**EvonaPulse** is an open-source event monitoring platform designed for developers.  
It allows you to receive, store, and visualize project-specific technical metrics through a simple HTTP API.

Built with **Spring Boot** and **Angular**, the platform is lightweight, self-hosted, and tailored for engineering teams that need observability without complexity.

## ✨ Features (MVP)

- 🔐 JWT-based user authentication  
- 📁 Project and metric management  
- 📡 Event reception via secure HTTP POST  
- 📊 Basic dashboards per project with real-time visualizations  

## 🚀 Getting Started (Development)

> ⚠️ Production setup is not available yet. The following instructions run the platform in development mode.

### Prerequisites

- Java 21  
- Node.js 20+  
- Angular CLI (`npm install -g @angular/cli`)

### Backend

If you're running the backend directly in IntelliJ, make sure to set the following environment variables in your run configuration:

```bash
JWT_EXPIRATION=28800000
JWT_SECRET=mySecret
SERVER_PORT=5000
```

### Frontend

Install dependencies and start project

```bash
cd client
npm i
npm run start
```

### Project Structure

```bash
EvonaPulse/
├── backend/         → Spring Boot API + DB
├── client/          → Angular frontend
│   └── src/assets/  → Contains logo and static assets
├── docs/            → External documentation
```

## 🛡 License

EvonaPulse is released under the [MIT License](./LICENSE).
