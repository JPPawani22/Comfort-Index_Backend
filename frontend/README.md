# Comfort Index Weather Application - Frontend

A modern Angular 21 frontend application with Tailwind CSS for displaying weather comfort data with Auth0 authentication.

## Features

- 🔐 **Auth0 Authentication** - Secure login with Auth0 Universal Login
- 🌤️ **Real-time Weather Data** - Live weather data from multiple cities
- 📊 **Comfort Index** - Advanced comfort calculations based on temperature, humidity, and wind
- 🎨 **Modern UI** - Beautiful, responsive design using Tailwind CSS
- 📱 **Mobile-First** - Fully responsive design for all devices
- ⚡ **Fast & Efficient** - Built with Angular 21 standalone components
- 🔄 **Real-time Updates** - Refresh weather data on demand

## Technology Stack

- **Angular 21** - Latest Angular framework with standalone components
- **TypeScript** - Type-safe development
- **Tailwind CSS** - Utility-first CSS framework
- **Auth0 Angular SDK** - Authentication and authorization
- **RxJS** - Reactive programming
- **SCSS** - CSS preprocessing

## Prerequisites

Before you begin, ensure you have the following installed:

- **Node.js** (v20 or higher) - [Download](https://nodejs.org/)
- **npm** (v10 or higher) - Comes with Node.js
- **Angular CLI** (v21) - Install globally: `npm install -g @angular/cli@21`

## Quick Start (3-Hour Setup)

### Step 1: Install Dependencies (5 minutes)

```bash
cd frontend
npm install
```

### Step 2: Configure Auth0 (15 minutes)

The project is pre-configured with Auth0 credentials. The existing configuration in `src/environments/environment.ts` uses:

- **Domain**: `dev-ny7i3r0vz4ns70x3.us.auth0.com`
- **Client ID**: `j5XPzIJg8Q9OuHhxzwVAt2RjxqsBmvtc`
- **Audience**: `https://weather-index.com`

These are already configured and working with the backend.

**Note**: For production use, you should create your own Auth0 application:

1. Go to [Auth0](https://auth0.com/) and sign up/login
2. Create a new Single Page Application
3. Set **Allowed Callback URLs**: `http://localhost:4200/callback`
4. Set **Allowed Logout URLs**: `http://localhost:4200`
5. Set **Allowed Web Origins**: `http://localhost:4200`
6. Create an API with identifier: `https://weather-index.com`
7. Update `src/environments/environment.ts` with your credentials

### Step 3: Start Backend (5 minutes)

In a separate terminal, start the Spring Boot backend:

```bash
cd ..  # Go to project root
./mvnw spring-boot:run
```

Backend will run on `http://localhost:8080`

### Step 4: Start Frontend (2 minutes)

```bash
npm start
```

Frontend will run on `http://localhost:4200`

### Step 5: Test the Application (3 minutes)

1. Open browser to `http://localhost:4200`
2. Click "Sign In with Auth0"
3. Create an account or login
4. View the weather dashboard
5. Click on any city to see details

**Total Setup Time: ~30 minutes**

## Project Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── core/                    # Core functionality
│   │   │   ├── guards/              # Route guards
│   │   │   ├── interceptors/        # HTTP interceptors
│   │   │   ├── services/            # Services
│   │   │   └── models/              # TypeScript models
│   │   ├── features/                # Feature modules
│   │   │   ├── auth/                # Authentication
│   │   │   ├── dashboard/           # Main dashboard
│   │   │   └── weather/             # Weather details
│   │   ├── shared/                  # Shared components
│   │   │   └── components/          # Reusable components
│   │   ├── app.ts                   # Root component
│   │   ├── app.config.ts            # App configuration
│   │   └── app.routes.ts            # Routes
│   ├── environments/                # Environment configs
│   ├── styles.scss                  # Global styles
│   └── main.ts                      # Entry point
├── tailwind.config.js               # Tailwind config
├── angular.json                     # Angular CLI config
└── package.json                     # Dependencies
```

## Available Scripts

- `npm start` - Start dev server (http://localhost:4200)
- `npm run build` - Build for production
- `npm run watch` - Build and watch for changes
- `npm test` - Run unit tests

## API Integration

The frontend connects to the backend at `http://localhost:8080/api/v1`:

### Endpoints
- `GET /weather/cities` - All cities weather
- `GET /weather/city/{cityId}` - Specific city weather
- `GET /weather/comfort-index/{cityId}` - Comfort index details
- `GET /weather/supported-cities` - Supported cities list

### Authentication
JWT tokens are automatically added to requests via the auth interceptor.

## Features Guide

### Login Page
- Modern, clean design
- Auth0 Universal Login
- Feature highlights
- Responsive layout

### Dashboard
- Weather cards grid
- Real-time data
- Color-coded comfort levels:
  - 🟢 **Good** (Green)
  - 🟡 **Moderate** (Yellow)
  - 🔴 **Poor** (Red)
- Search functionality
- Refresh button
- Click cards for details

### Weather Details
- Comprehensive metrics
- Temperature, humidity, wind, pressure
- Comfort index breakdown
- Visual progress bars
- Back navigation

### Navigation
- Responsive navbar
- User profile dropdown
- Login/Logout
- Mobile menu

## Customization

### Colors (tailwind.config.js)
```javascript
colors: {
  primary: '#3B82F6',      // Blue
  secondary: '#10B981',    // Green
  danger: '#EF4444',       // Red
  warning: '#F59E0B',      // Yellow
  good: '#10B981',
  moderate: '#F59E0B',
  poor: '#EF4444',
}
```

### Backend URL
Update in `src/environments/environment.ts`:
```typescript
apiUrl: 'http://localhost:8080/api/v1'
```

## Building for Production

```bash
npm run build
```

Output: `dist/frontend/browser/`

### Production Configuration

Update `src/environments/environment.prod.ts`:
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://your-api.com/api/v1',
  auth0: {
    domain: 'your-domain.auth0.com',
    clientId: 'your-client-id',
    authorizationParams: {
      redirect_uri: window.location.origin + '/callback',
      audience: 'https://weather-index.com'
    }
  }
};
```

## Deployment Options

- **Netlify**: `netlify deploy --prod --dir=dist/frontend/browser`
- **Vercel**: `vercel --prod`
- **Firebase**: `firebase deploy`
- **AWS S3**: Upload to S3 bucket
- **Azure**: Azure Static Web Apps
- **GitHub Pages**: GitHub Actions

## Troubleshooting

### Auth0 Errors

**"Invalid state"**
- Clear browser cache and try again

**"Audience is required"**
- Verify API audience in environment.ts

### API Errors

**CORS errors**
- Ensure backend allows `http://localhost:4200`
- Check `cors.allowed-origins` in backend

**401 Unauthorized**
- Verify Auth0 JWT config matches
- Check token is being sent

### Build Errors

**Tailwind not working**
- Verify `@tailwind` directives in styles.scss

**Module not found**
- Run `npm install` again
- Delete node_modules and reinstall

## Browser Support

✅ Chrome (latest)
✅ Firefox (latest)
✅ Safari (latest)
✅ Edge (latest)

## Tech Stack Details

### Angular 21 Features Used
- Standalone components
- Functional guards
- HTTP interceptor functions
- Signal-based reactivity (where applicable)
- Modern Angular routing

### RxJS Patterns
- Observable streams
- Operators (map, tap, catchError)
- BehaviorSubject for caching
- Proper unsubscription handling

### Tailwind CSS
- Utility-first approach
- Custom color palette
- Responsive design utilities
- Component styling

## Performance Optimizations

- Lazy loading (ready for future modules)
- HTTP caching via BehaviorSubject
- Optimized bundle size
- Production build optimizations
- Tree-shakeable imports

## Security Features

- JWT token authentication
- HTTP interceptor for token injection
- Protected routes with guards
- Secure Auth0 integration
- CORS protection

## Development Tips

1. **Hot Reload**: Changes auto-reload during `npm start`
2. **DevTools**: Use Angular DevTools browser extension
3. **Debugging**: Set breakpoints in browser DevTools
4. **API Testing**: Use browser Network tab
5. **Auth Testing**: Check Auth0 logs in dashboard

## Testing

The project includes test configurations. To add tests:

```bash
npm test
```

Test files should follow the pattern: `*.spec.ts`

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Commit changes: `git commit -m 'Add my feature'`
4. Push to branch: `git push origin feature/my-feature`
5. Submit a pull request

## Next Steps

After setup, you can:

1. **Customize Styling**: Modify Tailwind colors and design
2. **Add Features**: Implement weather history, favorites, etc.
3. **Add Tests**: Write unit and e2e tests
4. **Optimize**: Add caching strategies, PWA features
5. **Deploy**: Choose a hosting provider and deploy

## Resources

- [Angular Docs](https://angular.dev/)
- [Auth0 Angular SDK](https://github.com/auth0/auth0-angular)
- [Tailwind CSS](https://tailwindcss.com/)
- [RxJS Guide](https://rxjs.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)

## Support

For issues:
1. Check this README
2. Review console errors
3. Check Network tab for API errors
4. Verify Auth0 configuration
5. Ensure backend is running

## License

Part of the Fidenz Weather Analytics Assignment.

---

**Estimated Setup Time**: 30 minutes to fully operational application
**Skill Level**: Intermediate Angular/TypeScript knowledge helpful
**Production Ready**: Yes (with proper Auth0 credentials)
