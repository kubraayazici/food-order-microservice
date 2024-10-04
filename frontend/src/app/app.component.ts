import { Component } from '@angular/core';
import { Restaurant } from './dto/Restaurant';
import { MenuItem } from './dto/MenuItem';
import { RestaurantService } from './service/restaurant.service';
import { Observable } from 'rxjs';
import { CartService } from './service/cart.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Restaurant List';
  
  cartItemCount$: Observable<number> | undefined;
  
  constructor(private cartService: CartService) {}
  ngOnInit(): void {
    this.cartItemCount$ = this.cartService.getCartItemCount();
  }
}
