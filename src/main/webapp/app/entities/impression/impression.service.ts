import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IImpression } from 'app/shared/model/impression.model';

type EntityResponseType = HttpResponse<IImpression>;
type EntityArrayResponseType = HttpResponse<IImpression[]>;

@Injectable({ providedIn: 'root' })
export class ImpressionService {
    public resourceUrl = SERVER_API_URL + 'api/impressions';

    constructor(private http: HttpClient) {}

    create(impression: IImpression): Observable<EntityResponseType> {
        return this.http.post<IImpression>(this.resourceUrl, impression, { observe: 'response' });
    }

    update(impression: IImpression): Observable<EntityResponseType> {
        return this.http.put<IImpression>(this.resourceUrl, impression, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IImpression>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IImpression[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
