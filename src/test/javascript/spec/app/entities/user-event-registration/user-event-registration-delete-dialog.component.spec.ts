/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EventAppTestModule } from '../../../test.module';
import { UserEventRegistrationDeleteDialogComponent } from 'app/entities/user-event-registration/user-event-registration-delete-dialog.component';
import { UserEventRegistrationService } from 'app/entities/user-event-registration/user-event-registration.service';

describe('Component Tests', () => {
    describe('UserEventRegistration Management Delete Component', () => {
        let comp: UserEventRegistrationDeleteDialogComponent;
        let fixture: ComponentFixture<UserEventRegistrationDeleteDialogComponent>;
        let service: UserEventRegistrationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [UserEventRegistrationDeleteDialogComponent]
            })
                .overrideTemplate(UserEventRegistrationDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserEventRegistrationDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserEventRegistrationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
