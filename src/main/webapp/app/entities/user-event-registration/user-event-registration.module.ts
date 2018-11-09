import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EventAppSharedModule } from 'app/shared';
import { EventAppAdminModule } from 'app/admin/admin.module';
import {
    UserEventRegistrationComponent,
    UserEventRegistrationDetailComponent,
    UserEventRegistrationUpdateComponent,
    UserEventRegistrationDeletePopupComponent,
    UserEventRegistrationDeleteDialogComponent,
    userEventRegistrationRoute,
    userEventRegistrationPopupRoute
} from './';

const ENTITY_STATES = [...userEventRegistrationRoute, ...userEventRegistrationPopupRoute];

@NgModule({
    imports: [EventAppSharedModule, EventAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserEventRegistrationComponent,
        UserEventRegistrationDetailComponent,
        UserEventRegistrationUpdateComponent,
        UserEventRegistrationDeleteDialogComponent,
        UserEventRegistrationDeletePopupComponent
    ],
    entryComponents: [
        UserEventRegistrationComponent,
        UserEventRegistrationUpdateComponent,
        UserEventRegistrationDeleteDialogComponent,
        UserEventRegistrationDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EventAppUserEventRegistrationModule {}
