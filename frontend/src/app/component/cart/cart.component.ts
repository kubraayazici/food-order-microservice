import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { CartItem } from '../../dto/CartItem';
import { OrderService } from '../../service/order.service';
import { Router } from '@angular/router';
import { OrderItemRequest, OrderRequest } from '../../dto/order/order-request';
import { CartService } from '../../service/cart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartItems$: Observable<CartItem[]>;
  checkoutError: string = '';

  private readonly TAX_RATE = 0.08;

  constructor(
    private orderService: OrderService,
    private router: Router,
    private cartService: CartService
  ) {
    this.cartItems$ = this.cartService.getCartItems();
  }

  ngOnInit(): void {}
  removeFromCart(itemId: number): void {
    this.cartService.removeFromCart(itemId);
  }

  updateQuantity(itemId: number, quantity: number): void {
    this.cartService.updateItemQuantity(itemId, quantity);
  }

  getSubtotal(cartItems: CartItem[]): number {
    return cartItems.reduce((total, item) => total + item.menuItem.price * item.quantity, 0);
  }

  getTaxEstimate(subtotal: number): number {
    return subtotal * this.TAX_RATE;
  }

  getOrderTotal(subtotal: number): number {
    return subtotal + this.getTaxEstimate(subtotal);
  }

  checkout(cartItems: CartItem[]): void {
    if (cartItems.length === 0) {
      this.checkoutError = 'Your cart is empty. Please add items before checking out.';
      return;
    }
    this.router.navigate(['/shipping']);
  }
}
