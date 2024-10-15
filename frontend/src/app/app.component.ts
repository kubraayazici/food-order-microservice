import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { CartService } from './service/cart.service';
import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Restaurant List';
  
  cartItemCount$: Observable<number> | undefined;
  isLoggedIn: boolean = false;
  currentUser: string | null = null;
  isDropdownOpen: boolean = false;
  
  constructor(
    private cartService: CartService,
    private authService: AuthService
  ) {
    
  }
  ngOnInit(): void {
    this.cartItemCount$ = this.cartService.getCartItemCount();

    // Subscribe vào trạng thái đăng nhập
    this.authService.isLoggedIn$.subscribe(status => {
      this.isLoggedIn = status;
    });

     // Subscribe vào tên người dùng
     this.authService.currentUser$.subscribe(username => {
      this.currentUser = username;
    });
  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  logout() {
    this.authService.logout();
    this.isDropdownOpen = false;
  }
  
}
