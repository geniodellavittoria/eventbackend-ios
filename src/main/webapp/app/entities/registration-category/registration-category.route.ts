import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RegistrationCategory } from 'app/shared/model/registration-category.model';
import { RegistrationCategoryService } from './registration-category.service';
import { RegistrationCategoryComponent } from './registration-category.component';
import { RegistrationCategoryDetailComponent } from './registration-category-detail.component';
import { RegistrationCategoryUpdateComponent } from './registration-category-update.component';
import { RegistrationCategoryDeletePopupComponent } from './registration-category-delete-dialog.component';
import { IRegistrationCategory } from 'app/shared/model/registration-category.model';

@Injectable({ providedIn: 'root' })
export class RegistrationCategoryResolve implements Resolve<IRegistrationCategory> {
    constructor(private service: RegistrationCategoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<RegistrationCategory> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<RegistrationCategory>) => response.ok),
                map((registrationCategory: HttpResponse<RegistrationCategory>) => registrationCategory.body)
            );
        }
        return of(new RegistrationCategory());
    }
}

export const registrationCategoryRoute: Routes = [
    {
        path: 'registration-category',
        component: RegistrationCategoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RegistrationCategories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'registration-category/:id/view',
        component: RegistrationCategoryDetailComponent,
        resolve: {
            registrationCategory: RegistrationCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RegistrationCategories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'registration-category/new',
        component: RegistrationCategoryUpdateComponent,
        resolve: {
            registrationCategory: RegistrationCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RegistrationCategories'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'registration-category/:id/edit',
        component: RegistrationCategoryUpdateComponent,
        resolve: {
            registrationCategory: RegistrationCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RegistrationCategories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const registrationCategoryPopupRoute: Routes = [
    {
        path: 'registration-category/:id/delete',
        component: RegistrationCategoryDeletePopupComponent,
        resolve: {
            registrationCategory: RegistrationCategoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'RegistrationCategories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
