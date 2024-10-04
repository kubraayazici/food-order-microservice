import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RestaurantDetailComponent } from './component/restaurant-detail/restaurant-detail.component';
import { ListRestaurantComponent } from './component/list-restaurant/list-restaurant.component';
import { CartComponent } from './component/cart/cart.component';
import { OrderConfirmationComponent } from './component/order-confirmation/order-confirmation.component';
import { ShippingComponent } from './component/shipping/shipping.component';

const routes: Routes = [
  { path: '', component: ListRestaurantComponent },
  { path: 'restaurant/:id', component: RestaurantDetailComponent },
  { path : 'cart', component: CartComponent },
  { path: 'order-confirmation/:orderId', component: OrderConfirmationComponent },
  { path: 'shipping', component: ShippingComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
