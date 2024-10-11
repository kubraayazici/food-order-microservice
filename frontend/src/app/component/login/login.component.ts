import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginForm : FormGroup;
  error : string = '';
  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password:  ['', Validators.required]
    });
   }

  ngOnInit(): void {
    if (this.authService.currentUserValue) {
      this.router.navigate(['/']);
    }
  }

  onSubmit() {
    debugger;
    if (this.loginForm.invalid) {
      return;
    }

    this.authService.login(this.loginForm.value)
   
      .subscribe({
        next: () => {
          this.router.navigate(['/']);
        },
        error: error => {
          this.error = error.error.message || 'Login failed';
        }
      });
  }
}
