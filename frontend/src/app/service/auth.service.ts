import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, switchMap, tap, throwError } from 'rxjs';
import { AuthResponse } from '../dto/auth/AuthResponse';
import { environment } from '../../environments/enviroment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserService } from './user.service';
import { UserDTO } from '../dto/auth/UserDTO';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // private authUrl = environment.baseUrl + '/auth';
  private authUrl = 'http://localhost:8081/api/v1/auth';
  private tokenKey = 'auth_token';
  private jwtHelper = new JwtHelperService();

  // BehaviorSubject để theo dõi trạng thái đăng nhập và tên người dùng
  private loggedIn = new BehaviorSubject<boolean>(this.isLoggedIn());
  isLoggedIn$ = this.loggedIn.asObservable();

  private currentUser = new BehaviorSubject<string | null>(this.getUsernameFromToken());
  currentUser$ = this.currentUser.asObservable();

  constructor(private http: HttpClient
    ,private userService: UserService,
  ) { 
  }

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.authUrl}/login`, { username, password })
      .pipe(
        tap(response => {
          this.setToken(response.token);
          const decodedUsername = this.getUsernameFromToken();
          
          this.currentUser.next(decodedUsername);
          this.loggedIn.next(true);
          
          if (decodedUsername) {
            this.userService.getUserByUsername(decodedUsername).subscribe(user => {
              this.userService.setUserInfo(user);  // Store user information in the UserService
            });
          }
        }),
        catchError(this.handleError)
      );
  }

  register(username : string , email :string, password : string) : Observable<any> {
    return this.http.post(`${this.authUrl}/register`, {
      username,
      email,
      password
    }).pipe(
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
  
  // Error handler
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = '';

    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Client-side error: ${error.error.message}`;
    } else {
      // Server-side error
      const errorResponse = error.error;  // This is where your ErrorResponse will be
      // errorMessage = `Server-side error: ${errorResponse.message} (Status: ${errorResponse.status})\nDetails: ${errorResponse.details}`;
      errorMessage = errorResponse.message;
    }

    // Optionally, you can log or display the error message here
    // console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    this.loggedIn.next(false);
    this.currentUser.next(null);
    this.userService.clearUserInfo();
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
