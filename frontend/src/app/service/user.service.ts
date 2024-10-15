import { Injectable } from '@angular/core';
import { environment } from '../../environments/enviroment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userUrl = environment.baseUrl + '/users';
  constructor(
    private http: HttpClient
  ) { }

  getUserByUsername(username: string) {
    return this.http.get(`${this.userUrl}/${username}`);
  }
}
