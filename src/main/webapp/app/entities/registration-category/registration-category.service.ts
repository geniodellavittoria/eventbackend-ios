import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRegistrationCategory } from 'app/shared/model/registration-category.model';

type EntityResponseType = HttpResponse<IRegistrationCategory>;
type EntityArrayResponseType = HttpResponse<IRegistrationCategory[]>;

@Injectable({ providedIn: 'root' })
export class RegistrationCategoryService {
    public resourceUrl = SERVER_API_URL + 'api/registration-categories';

    constructor(private http: HttpClient) {}

    create(registrationCategory: IRegistrationCategory): Observable<EntityResponseType> {
        return this.http.post<IRegistrationCategory>(this.resourceUrl, registrationCategory, { observe: 'response' });
    }

    update(registrationCategory: IRegistrationCategory): Observable<EntityResponseType> {
        return this.http.put<IRegistrationCategory>(this.resourceUrl, registrationCategory, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRegistrationCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRegistrationCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
