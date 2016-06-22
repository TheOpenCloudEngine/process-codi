package org.uengine.codi.mw3.billing.catalog.api;

import org.uengine.codi.mw3.billing.exception.CatalogApiException;

/**
 * Created by uEngineYBS on 2016-06-20.
 */
public interface Block {

    /**
     * @return the {@code BlockType}
     */
    public BlockType getType();

    /**
     * @return the unit for that {@code Block} section.
     */
    public Unit getUnit();

    /**
     * @return the size of the block
     */
    public Double getSize();

    /**
     * @return the recurring {@code InternationalPrice} for that {@code Block} section.
     */
    public InternationalPrice getPrice();

    /**
     * @return the minimum number of {@code Unit} credits after which TopUp kicks in.
     * @throws CatalogApiException if the {#code Block} is not of type {@code BlockType.TOP_UP}
     */
    public Double getMinTopUpCredit() throws CatalogApiException;
}
