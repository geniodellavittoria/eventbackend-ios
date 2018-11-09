/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EventAppTestModule } from '../../../test.module';
import { RegistrationCategoryDeleteDialogComponent } from 'app/entities/registration-category/registration-category-delete-dialog.component';
import { RegistrationCategoryService } from 'app/entities/registration-category/registration-category.service';

describe('Component Tests', () => {
    describe('RegistrationCategory Management Delete Component', () => {
        let comp: RegistrationCategoryDeleteDialogComponent;
        let fixture: ComponentFixture<RegistrationCategoryDeleteDialogComponent>;
        let service: RegistrationCategoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [RegistrationCategoryDeleteDialogComponent]
            })
                .overrideTemplate(RegistrationCategoryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RegistrationCategoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RegistrationCategoryService);
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
