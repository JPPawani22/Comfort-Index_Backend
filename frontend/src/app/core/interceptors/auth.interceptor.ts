import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { mergeMap, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  // Check if request needs authentication
  if (req.url.includes('/api/v1/weather')) {
    return authService.getAccessTokenSilently().pipe(
      mergeMap(token => {
        const authReq = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
        return next(authReq);
      }),
      catchError(error => {
        console.error('Auth interceptor error:', error);
        // If token retrieval fails, proceed without token
        return next(req);
      })
    );
  }

  return next(req);
};
