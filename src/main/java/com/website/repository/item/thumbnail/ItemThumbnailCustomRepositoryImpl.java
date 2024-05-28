package com.website.repository.item.thumbnail;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.website.domain.attachment.Attachment;
import com.website.domain.item.Item;
import com.website.domain.item.ItemThumbnail;
import com.website.domain.item.QItemThumbnail;
import com.website.web.dto.response.item.ItemThumbnailResponse;
import com.website.web.dto.response.item.QItemThumbnailResponse;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.website.domain.item.QItemThumbnail.itemThumbnail;

@Repository
public class ItemThumbnailCustomRepositoryImpl implements ItemThumbnailCustomRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    public ItemThumbnailCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    public ItemThumbnailResponse findByItemId(Long itemId) {

        List<ItemThumbnailResponse> fetch = query.select(
                        new QItemThumbnailResponse(
                                itemThumbnail.id,
                                itemThumbnail.attachment.id,
                                itemThumbnail.item.id
                        )
                ).from(itemThumbnail)
                .where(itemThumbnail.item.id.eq(itemId)).fetch();

        if (fetch.size() == 0) {
            return null;
        }
        return fetch.get(0);
    }

    @Override
    public void updateThumbnail(Long itemId, Long imageId) {
        query.update(itemThumbnail)
                .set(itemThumbnail.attachment.id, imageId)
                .where(itemThumbnail.item.id.eq(itemId))
                .execute();
    }

    @Override
    public void insertItemThumbnail(Attachment attachment, Item item) {
        query.insert(itemThumbnail)
                .columns(itemThumbnail.attachment.id, itemThumbnail.item.id)
                .values(attachment.getId(), item.getId())
                .execute();
    }

    @Override
    public void deleteByItemId(Long itemId) {
        query.delete(itemThumbnail).where(itemThumbnail.item.id.eq(itemId)).execute();
    }

    @Override
    public void insertItemThumbnail(Long itemId, Long imageId) {
        query.insert(itemThumbnail)
                .set(itemThumbnail.attachment.id, itemThumbnail.item.id)
                .values(imageId, itemId)
                .execute();
    }

    //@Override
    //public void updateItemThumbnail(Long itemThumbnailId, Long attachmentIdParam, Long itemIdParam) {
    //    if (itemThumbnailId == null) {
    //        query.insert(itemThumbnail)
    //                .columns(itemThumbnail.attachment.id, itemThumbnail.item.id)
    //                .values(attachmentIdParam, itemIdParam)
    //                .execute();
    //    }
    //
    //    query.update(itemThumbnail)
    //            .set(itemThumbnail.attachment.id, attachmentIdParam)
    //            .set(itemThumbnail.item.id, itemIdParam)
    //            .where(itemThumbnail.id.eq(itemThumbnailId))
    //            .execute();
    //}

}
