import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EventAppSharedModule } from 'app/shared';
import {
    ImpressionComponent,
    ImpressionDetailComponent,
    ImpressionUpdateComponent,
    ImpressionDeletePopupComponent,
    ImpressionDeleteDialogComponent,
    impressionRoute,
    impressionPopupRoute
} from './';

const ENTITY_STATES = [...impressionRoute, ...impressionPopupRoute];

@NgModule({
    imports: [EventAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ImpressionComponent,
        ImpressionDetailComponent,
        ImpressionUpdateComponent,
        ImpressionDeleteDialogComponent,
        ImpressionDeletePopupComponent
    ],
    entryComponents: [ImpressionComponent, ImpressionUpdateComponent, ImpressionDeleteDialogComponent, ImpressionDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EventAppImpressionModule {}
