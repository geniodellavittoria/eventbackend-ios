/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EventAppTestModule } from '../../../test.module';
import { ImpressionDeleteDialogComponent } from 'app/entities/impression/impression-delete-dialog.component';
import { ImpressionService } from 'app/entities/impression/impression.service';

describe('Component Tests', () => {
    describe('Impression Management Delete Component', () => {
        let comp: ImpressionDeleteDialogComponent;
        let fixture: ComponentFixture<ImpressionDeleteDialogComponent>;
        let service: ImpressionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [ImpressionDeleteDialogComponent]
            })
                .overrideTemplate(ImpressionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ImpressionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ImpressionService);
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
