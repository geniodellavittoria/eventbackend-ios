import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EventAppSharedModule } from 'app/shared';
import {
    RegistrationCategoryComponent,
    RegistrationCategoryDetailComponent,
    RegistrationCategoryUpdateComponent,
    RegistrationCategoryDeletePopupComponent,
    RegistrationCategoryDeleteDialogComponent,
    registrationCategoryRoute,
    registrationCategoryPopupRoute
} from './';

const ENTITY_STATES = [...registrationCategoryRoute, ...registrationCategoryPopupRoute];

@NgModule({
    imports: [EventAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RegistrationCategoryComponent,
        RegistrationCategoryDetailComponent,
        RegistrationCategoryUpdateComponent,
        RegistrationCategoryDeleteDialogComponent,
        RegistrationCategoryDeletePopupComponent
    ],
    entryComponents: [
        RegistrationCategoryComponent,
        RegistrationCategoryUpdateComponent,
        RegistrationCategoryDeleteDialogComponent,
        RegistrationCategoryDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EventAppRegistrationCategoryModule {}
