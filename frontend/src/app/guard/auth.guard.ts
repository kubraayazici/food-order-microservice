import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { AuthService } from '../service/auth.service';

export const AuthGuard: CanActivateFn = (route, state): boolean | UrlTree=> {
  const router = inject(Router);
  const authService = inject(AuthService);

  if(authService.isLoggedIn()) {
    return true;
  }

  // Store the attempted URL for redirecting
  return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
};
