import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EventAppSharedModule } from 'app/shared';
import { EventAppAdminModule } from 'app/admin/admin.module';
import {
    EventComponent,
    EventDetailComponent,
    EventUpdateComponent,
    EventDeletePopupComponent,
    EventDeleteDialogComponent,
    eventRoute,
    eventPopupRoute
} from './';

const ENTITY_STATES = [...eventRoute, ...eventPopupRoute];

@NgModule({
    imports: [EventAppSharedModule, EventAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [EventComponent, EventDetailComponent, EventUpdateComponent, EventDeleteDialogComponent, EventDeletePopupComponent],
    entryComponents: [EventComponent, EventUpdateComponent, EventDeleteDialogComponent, EventDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EventAppEventModule {}
