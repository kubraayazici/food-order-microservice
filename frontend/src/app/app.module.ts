import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RestaurantDetailComponent } from './component/restaurant-detail/restaurant-detail.component';
import { ListRestaurantComponent } from './component/list-restaurant/list-restaurant.component';
import { CartComponent } from './component/cart/cart.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { OrderConfirmationComponent } from './component/order-confirmation/order-confirmation.component';
import { ShippingComponent } from './component/shipping/shipping.component';
import { LoginComponent } from './component/login/login.component';
import { AuthInterceptor } from './interceptor/auth-interceptor.interceptor';
import { RegisterComponent } from './component/register/register.component';
import { ProfileComponent } from './component/profile/profile.component';

@NgModule({
  declarations: [
    AppComponent,
    RestaurantDetailComponent,
    ListRestaurantComponent,
    CartComponent,
    OrderConfirmationComponent,
    ShippingComponent,
    LoginComponent,
    RegisterComponent,
    ProfileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    { provide : HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  
 }
