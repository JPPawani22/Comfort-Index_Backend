import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { CallbackComponent } from './features/auth/callback/callback.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { WeatherDetailComponent } from './features/weather/weather-detail/weather-detail.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'callback', component: CallbackComponent },
  { 
    path: 'dashboard', 
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  { 
    path: 'weather/:city', 
    component: WeatherDetailComponent,
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: '/login' }
];
