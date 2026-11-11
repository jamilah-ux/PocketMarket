package com.example.pocketmarket.fixtures

import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Product

object MockProducts {
    val items: List<Product> = listOf(
        Product(
            id = 1,
            title = "iPhone 9",
            description = "An apple mobile which is nothing like apple",
            price = 549.0,
            discountPercentage = 12.96,
            rating = 4.69,
            stock = 94,
            brand = "Apple",
            category = Category("smartphones"),
            thumbnail = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 2,
            title = "iPhone X",
            description = "SIM-Free, Model A19211 6.5-inch Super Retina HD display",
            price = 899.0,
            discountPercentage = 17.94,
            rating = 4.44,
            stock = 0,
            brand = "Apple",
            category = Category("smartphones"),
            thumbnail = "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 3,
            title = "Samsung Universe 9",
            description = "Samsung's new variant which goes beyond Galaxy",
            price = 1249.0,
            discountPercentage = 15.46,
            rating = 4.09,
            stock = 36,
            brand = "Samsung",
            category = Category("smartphones"),
            thumbnail = "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 4,
            title = "OPPOF19",
            description = "OPPO F19 is officially announced in April 2021",
            price = 280.0,
            discountPercentage = 17.91,
            rating = 4.3,
            stock = 123,
            brand = "OPPO",
            category = Category("smartphones"),
            thumbnail = "https://images.unsplash.com/photo-1580910051074-3eb694886505?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 5,
            title = "Huawei P30",
            description = "Huawei’s re-spun P30 Pro New Edition",
            price = 499.0,
            discountPercentage = 10.58,
            rating = 4.09,
            stock = 32,
            brand = "Huawei",
            category = Category("smartphones"),
            thumbnail = "https://images.unsplash.com/photo-1546054454-aa26e2b734c7?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 6,
            title = "MacBook Pro",
            description = "MacBook Pro 2021 with mini-LED display",
            price = 1749.0,
            discountPercentage = 11.02,
            rating = 4.57,
            stock = 83,
            brand = "Apple",
            category = Category("laptops"),
            thumbnail = "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 7,
            title = "Samsung Galaxy Book",
            description = "Samsung Galaxy Book S (2020) Laptop",
            price = 1499.0,
            discountPercentage = 4.15,
            rating = 4.25,
            stock = 50,
            brand = "Samsung",
            category = Category("laptops"),
            thumbnail = "https://images.unsplash.com/photo-1531297484001-80022131f5a1?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 8,
            title = "Microsoft Surface Laptop 4",
            description = "Style and speed. Stand out on HD video calls",
            price = 1499.0,
            discountPercentage = 10.23,
            rating = 4.43,
            stock = 68,
            brand = "Microsoft",
            category = Category("laptops"),
            thumbnail = "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 9,
            title = "Infinix INBOOK",
            description = "Infinix Inbook X1 Slim Core i3 10th Gen",
            price = 1099.0,
            discountPercentage = 11.83,
            rating = 4.54,
            stock = 96,
            brand = "Infinix",
            category = Category("laptops"),
            thumbnail = "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 10,
            title = "HP Pavilion 15-DK1056WM",
            description = "Gaming Laptop with 10th Gen Intel Core",
            price = 1099.0,
            discountPercentage = 6.18,
            rating = 4.43,
            stock = 89,
            brand = "HP",
            category = Category("laptops"),
            thumbnail = "https://images.unsplash.com/photo-1525547719571-a2d4ac8945e2?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 11,
            title = "Perfume Oil",
            description = "Mega Collection concentrated fragrance oil",
            price = 13.0,
            discountPercentage = 8.4,
            rating = 4.26,
            stock = 65,
            brand = "Impression of Acqua Di Gio",
            category = Category("fragrances"),
            thumbnail = "https://images.unsplash.com/photo-1547887537-6158d64c35b3?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 12,
            title = "Brown Perfume",
            description = "Royal Mirage Sport Brown Edition Eau De Cologne",
            price = 40.0,
            discountPercentage = 15.66,
            rating = 4.0,
            stock = 52,
            brand = "Royal Mirage",
            category = Category("fragrances"),
            thumbnail = "https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 13,
            title = "Fog Scent Xpressio",
            description = "Smell good all day long with Xpressio Perfume",
            price = 13.0,
            discountPercentage = 8.14,
            rating = 4.59,
            stock = 61,
            brand = "Fog Scent Expressio",
            category = Category("fragrances"),
            thumbnail = "https://images.unsplash.com/photo-1523293182086-7651a899d37f?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 14,
            title = "Non-Alcoholic Perfume",
            description = "Original Al-Rehab concentrated perfume 6ml",
            price = 120.0,
            discountPercentage = 15.6,
            rating = 4.21,
            stock = 114,
            brand = "Al Rehab",
            category = Category("fragrances"),
            thumbnail = "https://images.unsplash.com/photo-1588405748880-12d1d2a59f75?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 15,
            title = "Eau De Parfum Spray",
            description = "Genuine fragrance with intense notes",
            price = 30.0,
            discountPercentage = 10.99,
            rating = 4.7,
            stock = 105,
            brand = "Lord - Al-Rehab",
            category = Category("fragrances"),
            thumbnail = "https://images.unsplash.com/photo-1541643600914-78b084683601?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 16,
            title = "Hyaluronic Acid Serum",
            description = "L'Oreal Paris revitalift 1.5% pure hyaluronic acid",
            price = 19.0,
            discountPercentage = 13.31,
            rating = 4.83,
            stock = 110,
            brand = "L'Oreal Paris",
            category = Category("skincare"),
            thumbnail = "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 17,
            title = "Tree Oil 30ml",
            description = "Tea tree essential oil for clear, healthy skin",
            price = 12.0,
            discountPercentage = 4.09,
            rating = 4.52,
            stock = 78,
            brand = "Hemani Tea",
            category = Category("skincare"),
            thumbnail = "https://images.unsplash.com/photo-1608248597263-00079e96047a?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 18,
            title = "Oil Free Moisturizer",
            description = "Dermive Oil Free Moisturizer with SPF 20",
            price = 40.0,
            discountPercentage = 13.1,
            rating = 4.56,
            stock = 88,
            brand = "Dermive",
            category = Category("skincare"),
            thumbnail = "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 19,
            title = "Skin Beauty Serum",
            description = "Fair & Clear whitening glow serum with Vitamin C",
            price = 46.0,
            discountPercentage = 10.68,
            rating = 4.42,
            stock = 54,
            brand = "ROREC White Rice",
            category = Category("skincare"),
            thumbnail = "https://images.unsplash.com/photo-1617897903246-719242758050?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 20,
            title = "Freckle Treatment Cream",
            description = "Effective face lightening cream dark spots removal",
            price = 70.0,
            discountPercentage = 16.99,
            rating = 4.06,
            stock = 140,
            brand = "Fair & Clear",
            category = Category("skincare"),
            thumbnail = "https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 21,
            title = "Daal Masoor 500g",
            description = "Fine quality unpolished split red lentils",
            price = 20.0,
            discountPercentage = 4.81,
            rating = 4.44,
            stock = 133,
            brand = "Saaf & Khaas",
            category = Category("groceries"),
            thumbnail = "https://images.unsplash.com/photo-1515543237350-b3eea1ec8082?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 22,
            title = "Elbow Macaroni 400g",
            description = "Bake parlor delicious elbow macaroni",
            price = 14.0,
            discountPercentage = 15.58,
            rating = 4.57,
            stock = 146,
            brand = "Bake Parlor",
            category = Category("groceries"),
            thumbnail = "https://images.unsplash.com/photo-1621996346565-e3d5d6281274?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 23,
            title = "Orange Essence Flavour",
            description = "Concentrated orange food essence for baking",
            price = 14.0,
            discountPercentage = 8.04,
            rating = 4.77,
            stock = 26,
            brand = "Baking Food Flavor",
            category = Category("groceries"),
            thumbnail = "https://images.unsplash.com/photo-1618160702438-9b02ab6515c9?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 24,
            title = "cereals muesli fruit nuts",
            description = "Healthy breakfast meal full of vitamins",
            price = 46.0,
            discountPercentage = 16.8,
            rating = 4.94,
            stock = 113,
            brand = "fauji",
            category = Category("groceries"),
            thumbnail = "https://images.unsplash.com/photo-1586444248902-2f64eddc13df?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 25,
            title = "Gulab Powder 50g",
            description = "Dry rose flower organic herbal cosmetic powder",
            price = 70.0,
            discountPercentage = 13.58,
            rating = 4.87,
            stock = 47,
            brand = "Dry Rose",
            category = Category("groceries"),
            thumbnail = "https://images.unsplash.com/photo-1509358271058-acd01cc9386a?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 26,
            title = "Plant Hanger",
            description = "Handmade macrame plant hanger indoor hanging",
            price = 41.0,
            discountPercentage = 17.86,
            rating = 4.08,
            stock = 131,
            brand = "Boho Decor",
            category = Category("home-decoration"),
            thumbnail = "https://images.unsplash.com/photo-1512496015851-a90fb38ba796?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 27,
            title = "Flying Wooden Bird",
            description = "Handcrafted hanging decorative bird ornament",
            price = 51.0,
            discountPercentage = 15.58,
            rating = 4.41,
            stock = 17,
            brand = "Wooden Arts",
            category = Category("home-decoration"),
            thumbnail = "https://images.unsplash.com/photo-1544816155-12df9643f363?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 28,
            title = "3D LED Key Lamp",
            description = "Illusion key lamp 7 colors night lamp",
            price = 20.0,
            discountPercentage = 16.49,
            rating = 4.82,
            stock = 54,
            brand = "LED Lights",
            category = Category("home-decoration"),
            thumbnail = "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 29,
            title = "Handcrafted Wall Mirror",
            description = "Vintage antique carved decorative mirror",
            price = 35.0,
            discountPercentage = 12.0,
            rating = 4.33,
            stock = 0,
            brand = "Vintage Home",
            category = Category("home-decoration"),
            thumbnail = "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=500&auto=format&fit=crop"
        ),
        Product(
            id = 30,
            title = "Key Holder Wooden",
            description = "Attractive decorative hanging wooden key shelf",
            price = 30.0,
            discountPercentage = 2.92,
            rating = 4.92,
            stock = 54,
            brand = "Golden",
            category = Category("home-decoration"),
            thumbnail = "https://images.unsplash.com/photo-1538688525198-9b88f6f53126?w=500&auto=format&fit=crop"
        )
    )
}

fun generateLargeProductList(count: Int = 1000): List<Product> {
    val baseItems = MockProducts.items
    return (1..count).map { index ->
        val template = baseItems[(index - 1) % baseItems.size]
        template.copy(
            id = index.toLong(),
            title = "${template.title} #$index"
        )
    }
}