import { NgModule } from '@angular/core';

import { EventAppSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [EventAppSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [EventAppSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class EventAppSharedCommonModule {}
