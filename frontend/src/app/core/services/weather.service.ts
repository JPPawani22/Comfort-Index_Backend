import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject, tap, catchError, of } from 'rxjs';
import { WeatherResponse, ComfortIndexResponse, City } from '../models/weather.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WeatherService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;
  private weatherCache$ = new BehaviorSubject<WeatherResponse[]>([]);

  /**
   * Get weather data for all cities
   * @param forceRefresh - Force refresh from API bypassing cache
   */
  getCities(forceRefresh: boolean = false): Observable<WeatherResponse[]> {
    const params = new HttpParams().set('forceRefresh', forceRefresh.toString());
    
    return this.http.get<WeatherResponse[]>(`${this.apiUrl}/weather/cities`, { params }).pipe(
      tap(data => this.weatherCache$.next(data)),
      catchError(error => {
        console.error('Error fetching cities weather:', error);
        return of([]);
      })
    );
  }

  /**
   * Get weather data for a specific city
   * @param cityId - City ID from OpenWeatherMap
   * @param forceRefresh - Force refresh from API
   */
  getWeatherByCity(cityId: string, forceRefresh: boolean = false): Observable<WeatherResponse> {
    const params = new HttpParams().set('forceRefresh', forceRefresh.toString());
    
    return this.http.get<WeatherResponse>(`${this.apiUrl}/weather/city/${cityId}`, { params }).pipe(
      catchError(error => {
        console.error(`Error fetching weather for city ${cityId}:`, error);
        throw error;
      })
    );
  }

  /**
   * Get comfort index details for a city
   * @param cityId - City ID
   */
  getComfortIndex(cityId: string): Observable<ComfortIndexResponse> {
    return this.http.get<ComfortIndexResponse>(`${this.apiUrl}/weather/comfort-index/${cityId}`).pipe(
      catchError(error => {
        console.error(`Error fetching comfort index for city ${cityId}:`, error);
        throw error;
      })
    );
  }

  /**
   * Get list of supported cities
   */
  getSupportedCities(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/weather/supported-cities`).pipe(
      catchError(error => {
        console.error('Error fetching supported cities:', error);
        return of([]);
      })
    );
  }

  /**
   * Refresh weather data for all cities
   */
  refreshWeatherData(): Observable<WeatherResponse[]> {
    return this.getCities(true);
  }

  /**
   * Get cached weather data
   */
  getCachedWeather(): Observable<WeatherResponse[]> {
    return this.weatherCache$.asObservable();
  }

  /**
   * Get comfort level color class based on comfort score
   */
  getComfortLevelColor(comfortLevel: string): string {
    const level = comfortLevel.toLowerCase();
    if (level.includes('good') || level.includes('comfortable')) {
      return 'bg-good';
    } else if (level.includes('moderate')) {
      return 'bg-moderate';
    } else {
      return 'bg-poor';
    }
  }

  /**
   * Get comfort level text color class
   */
  getComfortLevelTextColor(comfortLevel: string): string {
    const level = comfortLevel.toLowerCase();
    if (level.includes('good') || level.includes('comfortable')) {
      return 'text-good';
    } else if (level.includes('moderate')) {
      return 'text-moderate';
    } else {
      return 'text-poor';
    }
  }
}
