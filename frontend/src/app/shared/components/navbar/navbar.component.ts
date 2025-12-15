import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  authService = inject(AuthService);
  isMenuOpen = false;
  isProfileOpen = false;

  user$ = this.authService.getUser();
  isAuthenticated$ = this.authService.isAuthenticated();

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  toggleProfile(): void {
    this.isProfileOpen = !this.isProfileOpen;
  }

  login(): void {
    this.authService.login();
  }

  logout(): void {
    this.authService.logout();
    this.isProfileOpen = false;
  }
}
