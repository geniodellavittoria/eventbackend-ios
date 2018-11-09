import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UserEventRegistration } from 'app/shared/model/user-event-registration.model';
import { UserEventRegistrationService } from './user-event-registration.service';
import { UserEventRegistrationComponent } from './user-event-registration.component';
import { UserEventRegistrationDetailComponent } from './user-event-registration-detail.component';
import { UserEventRegistrationUpdateComponent } from './user-event-registration-update.component';
import { UserEventRegistrationDeletePopupComponent } from './user-event-registration-delete-dialog.component';
import { IUserEventRegistration } from 'app/shared/model/user-event-registration.model';

@Injectable({ providedIn: 'root' })
export class UserEventRegistrationResolve implements Resolve<IUserEventRegistration> {
    constructor(private service: UserEventRegistrationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<UserEventRegistration> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<UserEventRegistration>) => response.ok),
                map((userEventRegistration: HttpResponse<UserEventRegistration>) => userEventRegistration.body)
            );
        }
        return of(new UserEventRegistration());
    }
}

export const userEventRegistrationRoute: Routes = [
    {
        path: 'user-event-registration',
        component: UserEventRegistrationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserEventRegistrations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-event-registration/:id/view',
        component: UserEventRegistrationDetailComponent,
        resolve: {
            userEventRegistration: UserEventRegistrationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserEventRegistrations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-event-registration/new',
        component: UserEventRegistrationUpdateComponent,
        resolve: {
            userEventRegistration: UserEventRegistrationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserEventRegistrations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-event-registration/:id/edit',
        component: UserEventRegistrationUpdateComponent,
        resolve: {
            userEventRegistration: UserEventRegistrationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserEventRegistrations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userEventRegistrationPopupRoute: Routes = [
    {
        path: 'user-event-registration/:id/delete',
        component: UserEventRegistrationDeletePopupComponent,
        resolve: {
            userEventRegistration: UserEventRegistrationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserEventRegistrations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
