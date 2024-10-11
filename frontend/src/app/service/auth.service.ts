import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { LoginRequest } from '../dto/auth/LoginRequest';
import { AuthResponse } from '../dto/auth/AuthResponse';
import { environment } from '../../environments/enviroment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = environment.baseUrl + '/auth';
  private currentUserSubject!: BehaviorSubject<string | null>;
  public currentUser!: Observable<string | null>;

  constructor(private http : HttpClient) {
    this.currentUserSubject = new BehaviorSubject<string | null>(localStorage.getItem('token'));
    this.currentUser = this.currentUserSubject.asObservable();
   }

   public get currentUserValue(): string | null {
      return this.currentUserSubject.value;
   }

   login(loginRequest: LoginRequest) : Observable<AuthResponse> {
    debugger;
      return this.http.post<AuthResponse>(`${this.authUrl}/login`, loginRequest)
      .pipe(
        map(response => {
            // store user details and jwt token in local storage to keep user logged in between page refreshes
            localStorage.setItem('token', response.token);
            this.currentUserSubject.next(response.token);
            return response;
        })
      );
   }

   logout() : void {
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
   }


}
