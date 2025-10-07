package com.unity.samples.agreements;

import com.unity.samples.BasePartnerComponentString;
import com.unity.samples.IPartner;


/**
 * Agreement details collection operations implementation class.
 */
public class AgreementDetailsCollectionOperations
        extends BasePartnerComponentString
        implements IAgreementDetailsCollection

{
    /**
     * Initializes a new instance of the AgreementDetailsCollectionOperations class.
     *
     * @param rootPartnerOperations The root partner operations instance.
     */
    public AgreementDetailsCollectionOperations( IPartner rootPartnerOperations )
    {
        super( rootPartnerOperations );
    }

    /**
     * Retrieves the agreement details.
     *
     * @return A list of agreement details.
     */
    public ResourceCollection<AgreementMetaData> get()
    {
        return null;
    }
}