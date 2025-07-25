package com.pvsrishabh.cakewake.features.home.data.local

import com.pvsrishabh.cakewake.features.home.domain.model.Cake
import com.pvsrishabh.cakewake.features.home.domain.model.Category
import com.pvsrishabh.cakewake.features.home.domain.model.SpecialOffer

object StaticHomeData {

    val categories = listOf(
        Category(
            id = "1",
            name = "Chocolate",
            imageUrl = "https://images.unsplash.com/photo-1549312524-d3123cea4e5e?w=150",
            color = "#8B4513"
        ),
        Category(
            id = "2",
            name = "Vanilla",
            imageUrl = "https://images.unsplash.com/photo-1571115764595-644a1f56a55c?w=150",
            color = "#F5DEB3"
        ),
        Category(
            id = "3",
            name = "Strawberry",
            imageUrl = "https://images.unsplash.com/photo-1565958011703-44f9829ba187?w=150",
            color = "#FFB6C1"
        ),
        Category(
            id = "4",
            name = "Black Forest",
            imageUrl = "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=150",
            color = "#2F4F2F"
        ),
        Category(
            id = "5",
            name = "Red Velvet",
            imageUrl = "https://images.unsplash.com/photo-1586040140378-b5c906f0e541?w=150",
            color = "#DC143C"
        )
    )

    val cakes = listOf(
        Cake(
            id = "1",
            name = "Chocolate Truffle Cake",
            price = 899.0,
            originalPrice = 1199.0,
            imageUrl = "https://images.unsplash.com/photo-1549312524-d3123cea4e5e?w=300",
            discount = "25% OFF",
            categoryId = "1"
        ),
        Cake(
            id = "2",
            name = "Vanilla Delight",
            price = 699.0,
            originalPrice = 899.0,
            imageUrl = "https://images.unsplash.com/photo-1571115764595-644a1f56a55c?w=300",
            discount = "22% OFF",
            categoryId = "2"
        ),
        Cake(
            id = "3",
            name = "Fresh Strawberry Cake",
            price = 1299.0,
            originalPrice = 1599.0,
            imageUrl = "https://images.unsplash.com/photo-1565958011703-44f9829ba187?w=300",
            discount = "19% OFF",
            categoryId = "3"
        ),
        Cake(
            id = "4",
            name = "Black Forest Special",
            price = 1499.0,
            originalPrice = 1899.0,
            imageUrl = "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=300",
            discount = "21% OFF",
            categoryId = "4"
        ),
        Cake(
            id = "5",
            name = "Red Velvet Classic",
            price = 1199.0,
            originalPrice = 1499.0,
            imageUrl = "https://images.unsplash.com/photo-1586040140378-b5c906f0e541?w=300",
            discount = "20% OFF",
            categoryId = "5"
        )
    )

    val specialOffers = listOf(
        SpecialOffer(
            id = "specials",
            title = "CakeWake Specials",
            description = "Best selling cakes",
            cakes = cakes.take(4),
            backgroundColor = "#FF6B9D"
        ),
        SpecialOffer(
            id = "love_bites",
            title = "CakeWake Love Bites",
            description = "Small portions, big taste",
            cakes = cakes.drop(1).take(3),
            backgroundColor = "#E91E63"
        ),
        SpecialOffer(
            id = "luxury",
            title = "CakeWake Luxury",
            description = "Premium luxury cakes",
            cakes = cakes.drop(2).take(3),
            backgroundColor = "#FFD700"
        ),
        SpecialOffer(
            id = "sugarless",
            title = "CakeWake Sugarless",
            description = "Healthy sugar-free options",
            cakes = cakes.drop(3).take(2),
            backgroundColor = "#4CAF50"
        ),
        SpecialOffer(
            id = "holi",
            title = "Holi Special",
            description = "Colorful festival cakes",
            cakes = cakes.take(2),
            backgroundColor = "#E91E63"
        ),
        SpecialOffer(
            id = "dewali",
            title = "Dewali Special",
            description = "Festival of lights special",
            cakes = cakes.drop(1).take(2),
            backgroundColor = "#FFC107"
        ),
        SpecialOffer(
            id = "eid",
            title = "Eid Special",
            description = "Celebrate with sweetness",
            cakes = cakes.drop(2).take(2),
            backgroundColor = "#4CAF50"
        )
    )
}