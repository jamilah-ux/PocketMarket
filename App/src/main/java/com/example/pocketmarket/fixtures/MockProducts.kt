package com.example.pocketmarket.fixtures
import com.example.pocketmarket.domain.model.Category
import com.example.pocketmarket.domain.model.Product


    object MockProducts {
        val items: List<Product> = listOf(
            Product(1, "iPhone 9", "An apple mobile which is nothing like apple", 549.0, 12.96, 4.69, 94, "Apple", Category("smartphones"), "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"),
            Product(2, "iPhone X", "SIM-Free, Model A19211 6.5-inch Super Retina HD display", 899.0, 17.94, 4.44, 0, "Apple", Category("smartphones"), "https://cdn.dummyjson.com/product-images/2/thumbnail.jpg"),
            Product(3, "Samsung Universe 9", "Samsung's new variant which goes beyond Galaxy", 1249.0, 15.46, 4.09, 36, "Samsung", Category("smartphones"), "https://cdn.dummyjson.com/product-images/3/thumbnail.jpg"),
            Product(4, "OPPOF19", "OPPO F19 is officially announced in April 2021", 280.0, 17.91, 4.3, 123, "OPPO", Category("smartphones"), "https://cdn.dummyjson.com/product-images/4/thumbnail.jpg"),
            Product(5, "Huawei P30", "Huawei’s re-spun P30 Pro New Edition", 499.0, 10.58, 4.09, 32, "Huawei", Category("smartphones"), "https://cdn.dummyjson.com/product-images/5/thumbnail.jpg"),
            Product(6, "MacBook Pro", "MacBook Pro 2021 with mini-LED display", 1749.0, 11.02, 4.57, 83, "Apple", Category("laptops"), "https://cdn.dummyjson.com/product-images/6/thumbnail.jpg"),
            Product(7, "Samsung Galaxy Book", "Samsung Galaxy Book S (2020) Laptop", 1499.0, 4.15, 4.25, 50, "Samsung", Category("laptops"), "https://cdn.dummyjson.com/product-images/7/thumbnail.jpg"),
            Product(8, "Microsoft Surface Laptop 4", "Style and speed. Stand out on HD video calls", 1499.0, 10.23, 4.43, 68, "Microsoft", Category("laptops"), "https://cdn.dummyjson.com/product-images/8/thumbnail.jpg"),
            Product(9, "Infinix INBOOK", "Infinix Inbook X1 Slim Core i3 10th Gen", 1099.0, 11.83, 4.54, 96, "Infinix", Category("laptops"), "https://cdn.dummyjson.com/product-images/9/thumbnail.jpg"),
            Product(10, "HP Pavilion 15-DK1056WM", "Gaming Laptop with 10th Gen Intel Core", 1099.0, 6.18, 4.43, 89, "HP", Category("laptops"), "https://cdn.dummyjson.com/product-images/10/thumbnail.jpg"),
            Product(11, "Perfume Oil", "Mega Collection concentrated fragrance oil", 13.0, 8.4, 4.26, 65, "Impression of Acqua Di Gio", Category("fragrances"), "https://cdn.dummyjson.com/product-images/11/thumbnail.jpg"),
            Product(12, "Brown Perfume", "Royal Mirage Sport Brown Edition Eau De Cologne", 40.0, 15.66, 4.0, 52, "Royal Mirage", Category("fragrances"), "https://cdn.dummyjson.com/product-images/12/thumbnail.jpg"),
            Product(13, "Fog Scent Xpressio", "Smell good all day long with Xpressio Perfume", 13.0, 8.14, 4.59, 61, "Fog Scent Expressio", Category("fragrances"), "https://cdn.dummyjson.com/product-images/13/thumbnail.jpg"),
            Product(14, "Non-Alcoholic Perfume", "Original Al-Rehab concentrated perfume 6ml", 120.0, 15.6, 4.21, 114, "Al Rehab", Category("fragrances"), "https://cdn.dummyjson.com/product-images/14/thumbnail.jpg"),
            Product(15, "Eau De Parfum Spray", "Genuine fragrance with intense notes", 30.0, 10.99, 4.7, 105, "Lord - Al-Rehab", Category("fragrances"), "https://cdn.dummyjson.com/product-images/15/thumbnail.jpg"),
            Product(16, "Hyaluronic Acid Serum", "L'Oreal Paris revitalift 1.5% pure hyaluronic acid", 19.0, 13.31, 4.83, 110, "L'Oreal Paris", Category("skincare"), "https://cdn.dummyjson.com/product-images/16/thumbnail.jpg"),
            Product(17, "Tree Oil 30ml", "Tea tree essential oil for clear, healthy skin", 12.0, 4.09, 4.52, 78, "Hemani Tea", Category("skincare"), "https://cdn.dummyjson.com/product-images/17/thumbnail.jpg"),
            Product(18, "Oil Free Moisturizer", "Dermive Oil Free Moisturizer with SPF 20", 40.0, 13.1, 4.56, 88, "Dermive", Category("skincare"), "https://cdn.dummyjson.com/product-images/18/thumbnail.jpg"),
            Product(19, "Skin Beauty Serum", "Fair & Clear whitening glow serum with Vitamin C", 46.0, 10.68, 4.42, 54, "ROREC White Rice", Category("skincare"), "https://cdn.dummyjson.com/product-images/19/thumbnail.jpg"),
            Product(20, "Freckle Treatment Cream", "Effective face lightening cream dark spots removal", 70.0, 16.99, 4.06, 140, "Fair & Clear", Category("skincare"), "https://cdn.dummyjson.com/product-images/20/thumbnail.jpg"),
            Product(21, "Daal Masoor 500g", "Fine quality unpolished split red lentils", 20.0, 4.81, 4.44, 133, "Saaf & Khaas", Category("groceries"), "https://cdn.dummyjson.com/product-images/21/thumbnail.jpg"),
            Product(22, "Elbow Macaroni 400g", "Bake parlor delicious elbow macaroni", 14.0, 15.58, 4.57, 146, "Bake Parlor", Category("groceries"), "https://cdn.dummyjson.com/product-images/22/thumbnail.jpg"),
            Product(23, "Orange Essence Flavour", "Concentrated orange food essence for baking", 14.0, 8.04, 4.77, 26, "Baking Food Flavor", Category("groceries"), "https://cdn.dummyjson.com/product-images/23/thumbnail.jpg"),
            Product(24, "cereals muesli fruit nuts", "Healthy breakfast meal full of vitamins", 46.0, 16.8, 4.94, 113, "fauji", Category("groceries"), "https://cdn.dummyjson.com/product-images/24/thumbnail.jpg"),
            Product(25, "Gulab Powder 50g", "Dry rose flower organic herbal cosmetic powder", 70.0, 13.58, 4.87, 47, "Dry Rose", Category("groceries"), "https://cdn.dummyjson.com/product-images/25/thumbnail.jpg"),
            Product(26, "Plant Hanger", "Handmade macrame plant hanger indoor hanging", 41.0, 17.86, 4.08, 131, "Boho Decor", Category("home-decoration"), "https://cdn.dummyjson.com/product-images/26/thumbnail.jpg"),
            Product(27, "Flying Wooden Bird", "Handcrafted hanging decorative bird ornament", 51.0, 15.58, 4.41, 17, "Wooden Arts", Category("home-decoration"), "https://cdn.dummyjson.com/product-images/27/thumbnail.jpg"),
            Product(28, "3D LED Key Lamp", "Illusion key lamp 7 colors night lamp", 20.0, 16.49, 4.82, 54, "LED Lights", Category("home-decoration"), "https://cdn.dummyjson.com/product-images/28/thumbnail.jpg"),
            Product(29, "Handcrafted Wall Mirror", "Vintage antique carved decorative mirror", 35.0, 12.0, 4.33, 0, "Vintage Home", Category("home-decoration"), "https://cdn.dummyjson.com/product-images/29/thumbnail.jpg"),
            Product(30, "Key Holder Wooden", "Attractive decorative hanging wooden key shelf", 30.0, 2.92, 4.92, 54, "Golden", Category("home-decoration"), "https://cdn.dummyjson.com/product-images/30/thumbnail.jpg")
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