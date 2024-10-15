import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { AuthResponse } from '../dto/auth/AuthResponse';
import { environment } from '../../environments/enviroment';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = environment.baseUrl + '/auth';
  private tokenKey = 'auth_token';
  private jwtHelper = new JwtHelperService();

  // BehaviorSubject để theo dõi trạng thái đăng nhập và tên người dùng
  private loggedIn = new BehaviorSubject<boolean>(this.isLoggedIn());
  isLoggedIn$ = this.loggedIn.asObservable();

  private currentUser = new BehaviorSubject<string | null>(this.getUsernameFromToken());
  currentUser$ = this.currentUser.asObservable();

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.authUrl}/login`, { username, password })
      .pipe(
        tap(response => {
          this.setToken(response.token);
          this.currentUser.next(username);
          this.loggedIn.next(true);
        }),
        catchError(this.handleError)
      );
  }
  
  private setToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    return token ? !this.jwtHelper.isTokenExpired(token) : false;
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = error.error.message;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    this.loggedIn.next(false);
    this.currentUser.next(null);
  }

  getUsernameFromToken(): string | null {
    const token = this.getToken();
    if(token && !this.jwtHelper.isTokenExpired(token)) {
      return this.jwtHelper.decodeToken(token).sub;
    }else{
      return null;
    }
  }

}
