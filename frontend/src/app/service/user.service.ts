import { Injectable } from '@angular/core';
import { environment } from '../../environments/enviroment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { UserDTO } from '../dto/auth/UserDTO';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userUrl = environment.baseUrl + '/users';

  private userSubject  = new BehaviorSubject<UserDTO | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(
    private http: HttpClient
  ) { }

  getUserByUsername(username: string): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.userUrl}/${username}`).pipe(
      tap(user => this.setUserInfo(user)),
      catchError(this.handleError)
    );
  }
  setUserInfo(user: UserDTO ) {
    this.userSubject.next(user);
  }

  getUserInfo(): UserDTO | null {
    return this.userSubject.getValue() ;
  }

  clearUserInfo() {
    this.userSubject.next(null);
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      errorMessage = error.error.message;
    } else {
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
