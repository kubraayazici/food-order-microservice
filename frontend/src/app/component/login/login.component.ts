import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  error : string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}
  

  onSubmit() {
    debugger
    this.authService.login(this.username, this.password).subscribe(
      response => {
        console.log(response );
        
        console.log('Login successful');
        // Navigate to home page or dashboard
        this.router.navigate(['/']);
      },
      error => {
        console.error('Login failed', error);
        error = 'Invalid username or password';
        // Handle error (show message to user)
      }
    );
  }
  
}
