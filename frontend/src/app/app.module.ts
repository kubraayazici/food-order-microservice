import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { RestaurantDetailComponent } from './component/restaurant-detail/restaurant-detail.component';
import { ListRestaurantComponent } from './component/list-restaurant/list-restaurant.component';
import { CartComponent } from './component/cart/cart.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { OrderConfirmationComponent } from './component/order-confirmation/order-confirmation.component';
import { ShippingComponent } from './component/shipping/shipping.component';

@NgModule({
  declarations: [
    AppComponent,
    RestaurantDetailComponent,
    ListRestaurantComponent,
    CartComponent,
    OrderConfirmationComponent,
    ShippingComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
  
 }
