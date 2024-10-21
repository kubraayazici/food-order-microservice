import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RestaurantDetailComponent } from './component/restaurant-detail/restaurant-detail.component';
import { ListRestaurantComponent } from './component/list-restaurant/list-restaurant.component';
import { CartComponent } from './component/cart/cart.component';
import { OrderConfirmationComponent } from './component/order-confirmation/order-confirmation.component';
import { ShippingComponent } from './component/shipping/shipping.component';
import { LoginComponent } from './component/login/login.component';
import { ProfileComponent } from './component/profile/profile.component';
import { AuthGuard } from './guard/auth.guard';
import { RegisterComponent } from './component/register/register.component';

const routes: Routes = [
  { path: '', component: ListRestaurantComponent },
  { path: 'restaurant/:id', component: RestaurantDetailComponent },
  { path : 'cart', component: CartComponent },
  { path: 'order-confirmation/:orderId', component: OrderConfirmationComponent ,canActivate: [AuthGuard]},
  { path: 'shipping', component: ShippingComponent ,canActivate: [AuthGuard]},
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent  ,canActivate: [AuthGuard]}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
