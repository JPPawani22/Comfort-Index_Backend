import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { WeatherService } from '../../../core/services/weather.service';
import { WeatherResponse, ComfortIndexResponse } from '../../../core/models/weather.model';
import { LoadingComponent } from '../../../shared/components/loading/loading.component';
import { ErrorComponent } from '../../../shared/components/error/error.component';

@Component({
  selector: 'app-weather-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, LoadingComponent, ErrorComponent],
  templateUrl: './weather-detail.component.html',
  styleUrls: ['./weather-detail.component.scss']
})
export class WeatherDetailComponent implements OnInit {
  private weatherService = inject(WeatherService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  cityId: string = '';
  weather: WeatherResponse | null = null;
  comfortIndex: ComfortIndexResponse | null = null;
  isLoading = true;
  error: string | null = null;

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.cityId = params['city'];
      if (this.cityId) {
        this.loadWeatherDetails();
      }
    });
  }

  loadWeatherDetails(forceRefresh: boolean = false): void {
    this.isLoading = true;
    this.error = null;

    this.weatherService.getWeatherByCity(this.cityId, forceRefresh).subscribe({
      next: (data) => {
        this.weather = data;
        this.loadComfortIndex();
      },
      error: (err) => {
        this.error = 'Failed to load weather details. Please try again.';
        this.isLoading = false;
        console.error('Error loading weather details:', err);
      }
    });
  }

  loadComfortIndex(): void {
    this.weatherService.getComfortIndex(this.cityId).subscribe({
      next: (data) => {
        this.comfortIndex = data;
        this.isLoading = false;
      },
      error: (err) => {
        // Comfort index is optional, don't show error
        console.warn('Could not load comfort index:', err);
        this.isLoading = false;
      }
    });
  }

  refreshData(): void {
    this.loadWeatherDetails(true);
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  getComfortLevelColor(comfortLevel: string): string {
    return this.weatherService.getComfortLevelColor(comfortLevel);
  }

  getComfortLevelTextColor(comfortLevel: string): string {
    return this.weatherService.getComfortLevelTextColor(comfortLevel);
  }

  getImpactColor(impact: number): string {
    if (impact >= 80) return 'text-good';
    if (impact >= 60) return 'text-moderate';
    return 'text-poor';
  }

  getProgressBarColor(impact: number): string {
    if (impact >= 80) return 'bg-good';
    if (impact >= 60) return 'bg-moderate';
    return 'bg-poor';
  }
}
