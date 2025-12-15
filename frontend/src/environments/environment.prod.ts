export const environment = {
  production: true,
  apiUrl: 'https://your-production-api.com/api/v1',
  auth0: {
    domain: 'dev-ny7i3r0vz4ns70x3.us.auth0.com',
    clientId: 'j5XPzIJg8Q9OuHhxzwVAt2RjxqsBmvtc',
    authorizationParams: {
      redirect_uri: window.location.origin + '/callback',
      audience: 'https://weather-index.com'
    },
    httpInterceptor: {
      allowedList: [
        'https://your-production-api.com/api/v1/*'
      ]
    }
  }
};
