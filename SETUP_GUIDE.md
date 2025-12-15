# Comfort Index Weather Application - Complete Setup Guide

This guide will help you set up and run the complete Comfort Index Weather Application with Angular 21 frontend and Spring Boot backend.

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Project Overview](#project-overview)
3. [Backend Setup](#backend-setup)
4. [Frontend Setup](#frontend-setup)
5. [Running the Application](#running-the-application)
6. [Testing the Application](#testing-the-application)
7. [Troubleshooting](#troubleshooting)

## Prerequisites

### Required Software

- **Java Development Kit (JDK) 17 or higher**
  - Download from: https://www.oracle.com/java/technologies/downloads/
  - Verify: `java -version`

- **Maven 3.6+ or Maven Wrapper (included)**
  - Verify: `mvn -version` or use `./mvnw`

- **Node.js 20+ and npm 10+**
  - Download from: https://nodejs.org/
  - Verify: `node --version` and `npm --version`

- **Angular CLI 21**
  - Install: `npm install -g @angular/cli@21`
  - Verify: `ng version`

### Optional but Recommended

- **Git** for version control
- **VS Code** or **IntelliJ IDEA** for development
- **Postman** or **curl** for API testing

## Project Overview

### Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Browser (User)                       │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────┐
│           Angular 21 Frontend (Port 4200)               │
│  ┌───────────────────────────────────────────────────┐  │
│  │ • Auth0 Authentication                            │  │
│  │ • Tailwind CSS Styling                           │  │
│  │ • Weather Dashboard                              │  │
│  │ • Comfort Index Visualization                    │  │
│  └───────────────────────────────────────────────────┘  │
└──────────────────────┬──────────────────────────────────┘
                       │ HTTP/REST
                       ↓
┌─────────────────────────────────────────────────────────┐
│          Spring Boot Backend (Port 8080)                │
│  ┌───────────────────────────────────────────────────┐  │
│  │ • REST API Endpoints                             │  │
│  │ • JWT Token Validation                           │  │
│  │ • Weather Data Processing                        │  │
│  │ • Comfort Index Calculation                      │  │
│  │ • Caching (Caffeine)                             │  │
│  └───────────────────────────────────────────────────┘  │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────┐
│            External Services                            │
│  • Auth0 (Authentication)                               │
│  • OpenWeatherMap API (Weather Data)                    │
└─────────────────────────────────────────────────────────┘
```

### Technology Stack

**Backend:**
- Spring Boot 3.2.0
- Spring Security with OAuth2 Resource Server
- Auth0 JWT validation
- Caffeine Caching
- OpenWeatherMap API integration
- Maven build system

**Frontend:**
- Angular 21 (Standalone Components)
- TypeScript 5.9
- Tailwind CSS 3.4
- Auth0 Angular SDK
- RxJS 7.8
- SCSS

## Backend Setup

### Step 1: Configure Environment Variables

The backend requires an OpenWeatherMap API key. You can:

1. **Get a free API key:**
   - Go to https://openweathermap.org/api
   - Sign up for a free account
   - Copy your API key

2. **Set the environment variable:**

   **Linux/Mac:**
   ```bash
   export OPENWEATHER_API_KEY=your_api_key_here
   ```

   **Windows (CMD):**
   ```cmd
   set OPENWEATHER_API_KEY=your_api_key_here
   ```

   **Windows (PowerShell):**
   ```powershell
   $env:OPENWEATHER_API_KEY="your_api_key_here"
   ```

   Or simply use the mock data by setting:
   ```bash
   export USE_MOCK_DATA=true
   ```

### Step 2: Configure Auth0 (Backend)

The backend is pre-configured with Auth0 credentials in `src/main/resources/application.properties`:

```properties
# Auth0 Configuration
auth0.domain=dev-ny7i3r0vz4ns70x3.us.auth0.com
auth0.issuer-uri=https://${auth0.domain}/
auth0.audience=https://weather-index.com
```

**For Production:**
1. Create your own Auth0 account at https://auth0.com
2. Create an API with identifier `https://weather-index.com`
3. Update the `auth0.domain` in application.properties

### Step 3: Build the Backend

```bash
# In the project root directory
./mvnw clean install
```

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

### Step 4: Run the Backend

```bash
./mvnw spring-boot:run
```

**Expected output:**
```
Started WeatherAnalyticsApplication in X.XXX seconds
```

The backend will be available at: **http://localhost:8080**

### Step 5: Verify Backend

Open a browser or use curl:

```bash
curl http://localhost:8080/api/v1/weather/health
```

**Expected response:**
```json
{
  "status": "UP",
  "timestamp": "2025-12-15T18:00:00"
}
```

## Frontend Setup

### Step 1: Install Dependencies

```bash
cd frontend
npm install
```

**Expected output:**
```
added XXX packages in XX.XXXs
```

### Step 2: Configure Auth0 (Frontend)

The frontend is pre-configured with Auth0 credentials in `src/environments/environment.ts`:

```typescript
auth0: {
  domain: 'dev-ny7i3r0vz4ns70x3.us.auth0.com',
  clientId: 'j5XPzIJg8Q9OuHhxzwVAt2RjxqsBmvtc',
  authorizationParams: {
    redirect_uri: 'http://localhost:4200/callback',
    audience: 'https://weather-index.com'
  }
}
```

**For Production:**
1. Create a Single Page Application in Auth0
2. Configure Allowed Callback URLs: `http://localhost:4200/callback`
3. Configure Allowed Logout URLs: `http://localhost:4200`
4. Configure Allowed Web Origins: `http://localhost:4200`
5. Update `environment.ts` with your credentials

### Step 3: Build the Frontend (Optional)

```bash
npm run build
```

**Expected output:**
```
✔ Building...
Application bundle generation complete. [X.XXX seconds]
```

### Step 4: Start Development Server

```bash
npm start
```

**Expected output:**
```
✔ Browser application bundle generation complete.
** Angular Live Development Server is listening on localhost:4200
✔ Compiled successfully.
```

The frontend will be available at: **http://localhost:4200**

## Running the Application

### Quick Start (Both Services)

**Terminal 1 - Backend:**
```bash
cd /path/to/Comfort-Index_Backend
./mvnw spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd /path/to/Comfort-Index_Backend/frontend
npm start
```

### Access the Application

1. **Open browser:** http://localhost:4200
2. **Login:** Click "Sign In with Auth0"
3. **Create account or login** using Auth0
4. **View Dashboard:** See weather data for multiple cities
5. **Click any city card** for detailed view

## Testing the Application

### Manual Testing Checklist

- [ ] **Login Flow**
  - Can access login page
  - Can sign in with Auth0
  - Redirects to dashboard after login
  - User profile shows in navbar

- [ ] **Dashboard**
  - Weather cards display correctly
  - Search functionality works
  - Refresh button updates data
  - Color-coded comfort levels visible
  - Responsive on mobile/tablet/desktop

- [ ] **Weather Details**
  - Click city card navigates to details
  - All weather metrics display
  - Comfort index breakdown shows
  - Progress bars visible
  - Back button returns to dashboard

- [ ] **Navigation**
  - Navbar shows on all pages
  - Profile dropdown works
  - Logout redirects to login
  - Mobile menu works

### API Testing

**Test Backend Endpoints:**

```bash
# Get all cities (requires authentication token)
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/api/v1/weather/cities

# Get specific city
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/api/v1/weather/city/1248991

# Get comfort index
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  http://localhost:8080/api/v1/weather/comfort-index/1248991
```

## Troubleshooting

### Backend Issues

**Problem:** `java.net.UnknownHostException: dev-ny7i3r0vz4ns70x3.us.auth0.com`
- **Cause:** Network cannot reach Auth0
- **Solution:** Check internet connection or use different Auth0 domain

**Problem:** `OPENWEATHER_API_KEY not found`
- **Cause:** API key not set
- **Solution:** Set environment variable or enable mock data

**Problem:** Port 8080 already in use
- **Solution:** Stop other services or change port in `application.properties`:
  ```properties
  server.port=8081
  ```

### Frontend Issues

**Problem:** `Cannot connect to backend`
- **Cause:** Backend not running
- **Solution:** Start backend first on port 8080

**Problem:** `Auth0 "Invalid state" error`
- **Cause:** Cached authentication state
- **Solution:** Clear browser cache and cookies

**Problem:** `Tailwind styles not applied`
- **Cause:** PostCSS not configured
- **Solution:** Verify `postcss.config.js` exists and run `npm install` again

**Problem:** Port 4200 already in use
- **Solution:** Kill the process or use different port:
  ```bash
  ng serve --port 4201
  ```

### CORS Issues

**Problem:** CORS errors in browser console
- **Solution:** Verify backend CORS configuration in `application.properties`:
  ```properties
  cors.allowed-origins=http://localhost:4200
  ```

### Auth Token Issues

**Problem:** 401 Unauthorized errors
- **Cause:** Token not being sent or invalid
- **Solution:** 
  1. Check Auth0 configuration matches in both frontend and backend
  2. Verify audience is correct
  3. Check token in browser DevTools → Network → Headers

## Production Deployment

### Backend Deployment

1. **Build JAR:**
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **Deploy to:**
   - AWS Elastic Beanstalk
   - Heroku
   - Google Cloud Run
   - Azure App Service

3. **Set environment variables:**
   - `OPENWEATHER_API_KEY`
   - `AUTH0_CLIENT_ID`
   - `AUTH0_CLIENT_SECRET`
   - `cors.allowed-origins` (production frontend URL)

### Frontend Deployment

1. **Build for production:**
   ```bash
   npm run build
   ```

2. **Deploy `dist/frontend/browser` to:**
   - Netlify
   - Vercel
   - Firebase Hosting
   - AWS S3 + CloudFront
   - Azure Static Web Apps

3. **Update `environment.prod.ts`:**
   - Production API URL
   - Production Auth0 credentials
   - Update Auth0 callback URLs

## Environment Variables Reference

### Backend (application.properties)

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| OPENWEATHER_API_KEY | OpenWeatherMap API key | Mock data | No |
| USE_MOCK_DATA | Use mock weather data | true | No |
| AUTH0_CLIENT_ID | Auth0 Client ID | Configured | Yes |
| AUTH0_CLIENT_SECRET | Auth0 Client Secret | Configured | Yes |
| cors.allowed-origins | Allowed frontend origins | localhost:4200 | Yes |

### Frontend (environment.ts)

| Variable | Description | Example |
|----------|-------------|---------|
| auth0.domain | Auth0 tenant domain | dev-xxx.us.auth0.com |
| auth0.clientId | Auth0 client ID | abc123... |
| auth0.audience | API identifier | https://weather-index.com |
| apiUrl | Backend API URL | http://localhost:8080/api/v1 |

## Architecture Decisions

### Why Auth0?
- Industry-standard authentication
- Built-in security features
- Easy integration with Angular and Spring Boot
- Supports social login
- MFA support out of the box

### Why Tailwind CSS?
- Utility-first approach
- Fast development
- Minimal CSS
- Consistent design system
- Great responsive utilities

### Why Angular 21?
- Latest features (standalone components)
- Strong TypeScript support
- Excellent tooling
- Enterprise-ready
- Great performance

### Why Spring Boot?
- Robust and battle-tested
- Easy OAuth2/JWT integration
- Great ecosystem
- Production-ready
- Excellent documentation

## Support and Resources

- **Backend Docs:** See `src/main/java/com/fidenz/weather/`
- **Frontend Docs:** See `frontend/README.md`
- **Swagger UI:** http://localhost:8080/api/swagger-ui.html (when backend is running)
- **Angular Docs:** https://angular.dev/
- **Auth0 Docs:** https://auth0.com/docs
- **Tailwind Docs:** https://tailwindcss.com/docs

## License

This project is part of the Fidenz Weather Analytics Assignment.

---

**Estimated Setup Time:** 30-45 minutes
**Difficulty Level:** Intermediate
**Last Updated:** December 15, 2025
