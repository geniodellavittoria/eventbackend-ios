import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Impression } from 'app/shared/model/impression.model';
import { ImpressionService } from './impression.service';
import { ImpressionComponent } from './impression.component';
import { ImpressionDetailComponent } from './impression-detail.component';
import { ImpressionUpdateComponent } from './impression-update.component';
import { ImpressionDeletePopupComponent } from './impression-delete-dialog.component';
import { IImpression } from 'app/shared/model/impression.model';

@Injectable({ providedIn: 'root' })
export class ImpressionResolve implements Resolve<IImpression> {
    constructor(private service: ImpressionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Impression> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Impression>) => response.ok),
                map((impression: HttpResponse<Impression>) => impression.body)
            );
        }
        return of(new Impression());
    }
}

export const impressionRoute: Routes = [
    {
        path: 'impression',
        component: ImpressionComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Impressions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'impression/:id/view',
        component: ImpressionDetailComponent,
        resolve: {
            impression: ImpressionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Impressions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'impression/new',
        component: ImpressionUpdateComponent,
        resolve: {
            impression: ImpressionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Impressions'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'impression/:id/edit',
        component: ImpressionUpdateComponent,
        resolve: {
            impression: ImpressionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Impressions'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const impressionPopupRoute: Routes = [
    {
        path: 'impression/:id/delete',
        component: ImpressionDeletePopupComponent,
        resolve: {
            impression: ImpressionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Impressions'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
