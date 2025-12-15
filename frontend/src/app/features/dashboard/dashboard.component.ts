import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { WeatherService } from '../../core/services/weather.service';
import { WeatherResponse } from '../../core/models/weather.model';
import { LoadingComponent } from '../../shared/components/loading/loading.component';
import { ErrorComponent } from '../../shared/components/error/error.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, LoadingComponent, ErrorComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  private weatherService = inject(WeatherService);
  private router = inject(Router);

  weatherData: WeatherResponse[] = [];
  isLoading = true;
  error: string | null = null;
  searchQuery = '';
  lastUpdated: Date = new Date();

  ngOnInit(): void {
    this.loadWeatherData();
  }

  loadWeatherData(forceRefresh: boolean = false): void {
    this.isLoading = true;
    this.error = null;

    this.weatherService.getCities(forceRefresh).subscribe({
      next: (data) => {
        this.weatherData = data;
        this.isLoading = false;
        this.lastUpdated = new Date();
      },
      error: (err) => {
        this.error = 'Failed to load weather data. Please try again later.';
        this.isLoading = false;
        console.error('Error loading weather data:', err);
      }
    });
  }

  refreshData(): void {
    this.loadWeatherData(true);
  }

  getComfortLevelColor(comfortLevel: string): string {
    return this.weatherService.getComfortLevelColor(comfortLevel);
  }

  getComfortLevelTextColor(comfortLevel: string): string {
    return this.weatherService.getComfortLevelTextColor(comfortLevel);
  }

  viewCityDetails(cityId: string): void {
    this.router.navigate(['/weather', cityId]);
  }

  getFilteredWeatherData(): WeatherResponse[] {
    if (!this.searchQuery) {
      return this.weatherData;
    }

    const query = this.searchQuery.toLowerCase();
    return this.weatherData.filter(weather =>
      weather.city_name.toLowerCase().includes(query) ||
      weather.country.toLowerCase().includes(query)
    );
  }

  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchQuery = input.value;
  }

  getRankBadgeColor(rank: number): string {
    if (rank === 1) return 'bg-yellow-400 text-yellow-900';
    if (rank === 2) return 'bg-gray-300 text-gray-900';
    if (rank === 3) return 'bg-orange-400 text-orange-900';
    return 'bg-gray-200 text-gray-700';
  }
}
