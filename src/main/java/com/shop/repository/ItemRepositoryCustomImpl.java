package com.shop.repository;


import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory; //동적쿼리 사용하기 위해 JPAQueryFactory 변수 선
    //생성자
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em); //JPAQueryFactory 실직적인 객체가 만들어짐
    }
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ?
                null : QItem.item.itemSellStatus.eq(searchSellStatus); // itemsellStatus itemSellStatus이아닌데 원인찾기
        //itemsellStatus null이면 null리턴 null 아니면SELL, SOLD 둘중 하나 리턴
    }
    private BooleanExpression regDtsAfter(String searchDateType){ //all, id, iw, 1m, 6m
        LocalDateTime dateTime = LocalDateTime.now(); //현재시간추출해서 변수대입

        if (StringUtils.equals("all",searchDateType) || searchDateType == null){
            return null;
        }
        else if (StringUtils.equals("1d",searchDateType)) {
            dateTime = dateTime.minusDays(1);
        }
        else if (StringUtils.equals("1w",searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        }
        else if (StringUtils.equals("1m",searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        }
        else if (StringUtils.equals("6m",searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
        //date time을 시간에 맞게 세팅 후 싯간에 맞는 등록된 상품이 조회하도록 조건값 반
    }
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if (StringUtils.equals("itemNm",searchBy)){//상품명
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        }
        else if (StringUtils.equals("createdBy",searchBy)){
            return QItem.item.createdBy.like("%"+searchQuery+"%"); //createBy createdBy 가 아닌이유 찾기
        }
        return null;
    }
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QueryResults<Item> results = queryFactory.selectFrom(QItem.item).
                where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }
    private  BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%"+searchQuery+"%");
    }
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        //select  i.id, id.itemNm, i.itemDetail, im.itemImg, i.price from item i, itemImg im join i.id = im.itemId
        //where im.repImgYn = "Y" and i.itemNm like %searchQuery% order by i.id desc
        //QMainItemDto @QueryProjection을 사용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemNm,
                        item.itemDetail,itemImg.imgUrl,item.price))
                //join 내부조인 .repImgYn.eq("Y") 대표 이미지만 가저온다
                .from(itemImg).join(itemImg.item, item).where(itemImg.repImgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
