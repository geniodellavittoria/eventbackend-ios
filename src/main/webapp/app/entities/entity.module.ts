import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { EventAppRegistrationCategoryModule } from './registration-category/registration-category.module';
import { EventAppUserEventRegistrationModule } from './user-event-registration/user-event-registration.module';
import { EventAppCategoryModule } from './category/category.module';
import { EventAppEventModule } from './event/event.module';
import { EventAppImpressionModule } from './impression/impression.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        EventAppRegistrationCategoryModule,
        EventAppUserEventRegistrationModule,
        EventAppCategoryModule,
        EventAppEventModule,
        EventAppImpressionModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EventAppEntityModule {}
