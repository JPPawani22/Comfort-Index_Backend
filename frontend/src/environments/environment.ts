export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1',
  auth0: {
    domain: 'dev-ny7i3r0vz4ns70x3.us.auth0.com',
    clientId: 'j5XPzIJg8Q9OuHhxzwVAt2RjxqsBmvtc',
    authorizationParams: {
      redirect_uri: 'http://localhost:4200/callback',
      audience: 'https://weather-index.com'
    },
    httpInterceptor: {
      allowedList: [
        'http://localhost:8080/api/v1/*'
      ]
    }
  }
};
