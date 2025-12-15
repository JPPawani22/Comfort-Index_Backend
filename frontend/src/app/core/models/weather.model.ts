export interface WeatherResponse {
  city_id: string;
  city_name: string;
  country: string;
  temperature: number;
  feels_like: number;
  humidity: number;
  pressure: number;
  wind_speed: number;
  cloudiness: number;
  visibility: number;
  weather_description: string;
  weather_icon: string;
  comfort_score: number;
  comfort_level: string;
  rank: number;
  cache_status: string;
  timestamp: string;
}

export interface ComfortIndexResponse {
  comfort_score: number;
  comfort_level: string;
  temperature_impact: number;
  humidity_impact: number;
  wind_impact: number;
}

export interface City {
  id: string;
  name: string;
  country: string;
}

export type ComfortLevel = 'Good' | 'Moderate' | 'Poor' | 'Comfortable' | 'Uncomfortable';
