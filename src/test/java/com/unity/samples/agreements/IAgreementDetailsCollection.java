package com.unity.samples.agreements;



/**
 * Encapsulates the operations on the agreement metadata collection.
 */
public interface IAgreementDetailsCollection
{
    /**
     * Retrieves all current agreement metadata.
     *
     * @return The current agreement metadata.
     */
    ResourceCollection<AgreementMetaData> get();
}