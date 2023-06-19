package com.example.tmall.controller;

import com.example.tmall.comparator.*;
import com.example.tmall.pojo.*;
import com.example.tmall.service.*;
import com.example.tmall.util.Result;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 作者：cjy
 * 类名：ForeRESTController
 * 全路径类名：com.example.tmall.controller.ForeRESTController
 * 父类或接口：
 * 描述：前台控制类
 */
@RestController
public class ForeRESTController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    OrderService orderService;

    /**
     * 方法名：home
     * 传入参数：
     * 异常类型：
     * 返回类型：@return {@link Object }
     * 描述：主页面，列出分类和对应产品
     */
    @GetMapping("/forehome")
    public Object home() {
        List<Category> cs= categoryService.list();
        productService.fill(cs);
        productService.fillByRow(cs);
        categoryService.removeCategoryFromProduct(cs);
        return cs;
    }

    /**
     * 方法名：register
     * 传入参数：@param user 用户
     * 异常类型：
     * 返回类型：@return {@link Object }
     * 描述：注册
     */
    @PostMapping("/foreregister")
    public Object register(@RequestBody User user) {
        String name =  user.getName();
        String password = user.getPassword();
        name = HtmlUtils.htmlEscape(name);// 对用户名进行HTML转义，防止XSS攻击
        user.setName(name);
        boolean exist = userService.isExist(name);

        if(exist){
            String message ="用户名已经被使用,不能使用";
            return Result.fail(message);
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 100;
        String algorithmName = Sha256Hash.ALGORITHM_NAME;

        String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);

        return Result.success();
    }

    /**
     * 方法名：login
     * 传入参数：@param userParam 用户参数
     *         @param session 会话
     * 异常类型：
     * 返回类型：@return {@link Object }
     * 描述：登录
     */
    @PostMapping("/forelogin")
    public Object login(@RequestBody User userParam, HttpSession session) {
        String name = userParam.getName();
        name = HtmlUtils.htmlEscape(name); // 对用户名进行HTML转义，防止XSS攻击

        Subject subject = SecurityUtils.getSubject();
        //用户的身份验证凭证
        UsernamePasswordToken token = new UsernamePasswordToken(name, userParam.getPassword());

        try {
            subject.login(token); // 执行身份验证登录操作
            User user = userService.getByName(name); // 根据用户名获取用户信息
            session.setAttribute("user", user); // 将用户信息存入Session
            return Result.success(); // 登录成功，返回成功结果
        } catch (AuthenticationException e) {
            String message = "账号密码错误";
            return Result.fail(message); // 登录失败，返回失败结果
        }
    }


    /**
     * 方法名：product
     * 传入参数：@param pid pid
     * 异常类型：
     * 返回类型：@return {@link Object }
     * 描述：产品详情消息
     */
    @GetMapping("/foreproduct/{pid}")
    public Object product(@PathVariable("pid") int pid) {
        Product product = productService.get(pid);

        List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
        List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);

        List<PropertyValue> pvs = propertyValueService.list(product);
        List<Review> reviews = reviewService.list(product);
        productService.setSaleAndReviewNumber(product);
        productImageService.setFirstProductImage(product);

        Map<String,Object> map= new HashMap<>();
        map.put("product", product);
        map.put("pvs", pvs);
        map.put("reviews", reviews);

        return Result.success(map);
    }

    /**
     * 方法名：checkLogin
     * 传入参数：
     * 异常类型：
     * 返回类型：@return {@link Object }
     * 描述：检查登录
     */
    @GetMapping("forecheckLogin")
    public Object checkLogin() {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated())
            return Result.success();
        else
            return Result.fail("未登录");
    }

    /**
     * 方法名：category
     * 传入参数：@param cid cid
     *         @param sort 排序
     * 异常类型：
     * 返回类型：@return {@link Object }
     * 描述：分类产品排序
     */
    @GetMapping("forecategory/{cid}")
    public Object category(@PathVariable int cid,String sort) {
        Category c = categoryService.get(cid);
        productService.fill(c);
        productService.setSaleAndReviewNumber(c.getProducts());
        categoryService.removeCategoryFromProduct(c);

        if(null!=sort){
            switch(sort){
                case "review":
                    Collections.sort(c.getProducts(),new ProductReviewComparator());
                    break;
                case "date" :
                    Collections.sort(c.getProducts(),new ProductDateComparator());
                    break;

                case "saleCount" :
                    Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                    break;

                case "price":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;

                case "all":
                    Collections.sort(c.getProducts(),new ProductAllComparator());
                    break;
            }
        }

        return c;
    }

    /**
     * 方法名：search
     * 传入参数：@param keyword 关键字
     * 异常类型：
     * 返回类型：@return {@link Object }
     * 描述：搜索
     */
    @PostMapping("foresearch")
    public Object search( String keyword){
        if(null==keyword)
            keyword = "";
        List<Product> ps= productService.search(keyword,0,20);

        productImageService.setFirstProductImages(ps);
        productService.setSaleAndReviewNumber(ps);
        return ps;
    }

    /**
     * 方法名：buyone
     * 传入参数：@param pid 产品id
     *         @param num 数量
     *         @param session 会话
     * 异常类型：
     * 返回类型：@return {@link Object }
     * 描述：购买一款产品
     */
    @GetMapping("forebuyone")
    public Object buyone(int pid, int num, HttpSession session) {
        return buyoneAndAddCart(pid,num,session);
    }


    /**
     * 方法名：buyoneAndCart
     * 传入参数：@param pid 产品id
     *         @param num 数量
     *         @param session 会话
     * 异常类型：
     * 返回类型：@return {@link Object }
     * 描述：购买一款产品并加入购物车
     */
    private int buyoneAndAddCart(int pid, int num, HttpSession session) {
        // 获取商品信息
        Product product = productService.get(pid);
        int oiid = 0;

        // 获取用户信息
        User user =(User) session.getAttribute("user");
        boolean found = false;
        List<OrderItem> ois = orderItemService.listByUser(user);

        for (OrderItem oi : ois) {
            // 如果已经存在该商品，则更新数量
            if(oi.getProduct().getId()==product.getId()){
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);
                found = true;
                oiid = oi.getId();
                break;
            }
        }

        // 如果不存在该商品，则创建新订单项并加入购物车
        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUser(user);
            oi.setProduct(product);
            oi.setNumber(num);
            orderItemService.add(oi);
            oiid = oi.getId();
        }
        return oiid;
    }
    /**
     * 处理前台购买请求的方法
     * @param oiid 购买的订单项ID数组
     * @param session HttpSession对象
     * @return 返回购买结果的JSON数据
     */
    @GetMapping("forebuy")
    public Object buy(String[] oiid, HttpSession session) {
        // 创建一个空的订单项列表
        List<OrderItem> orderItems = new ArrayList<>();
        // 创建一个初始值为0的总金额对象
        Money total = Money.of(CurrencyUnit.of("CNY"), 0);

        // 遍历传入的订单项ID数组
        for (String strid : oiid) {
            int id = Integer.parseInt(strid);
            // 根据订单项ID获取订单项对象
            OrderItem oi = orderItemService.get(id);
            BigDecimal orderItemNumber = BigDecimal.valueOf(oi.getNumber());
            // 计算订单项的金额，并累加到总金额中
            total = total.plus(oi.getProduct().getPromotePrice().getAmount().multiply(orderItemNumber));
            // 将订单项添加到创建的订单项列表中
            orderItems.add(oi);
        }

        // 设置订单项中的产品图片
        productImageService.setFirstProdutImagesOnOrderItems(orderItems);

        // 将订单项列表保存到会话中
        session.setAttribute("ois", orderItems);

        // 创建一个Map对象用于存储返回的数据
        Map<String, Object> map = new HashMap<>();
        map.put("orderItems", orderItems);
        map.put("total", total);

        // 返回购买结果的JSON数据
        return Result.success(map);
    }
    @GetMapping("foreaddCart")
    public Object addCart(int pid, int num, HttpSession session) {
        buyoneAndAddCart(pid,num,session);
        return Result.success();
    }
    @GetMapping("forecart")
    public Object cart(HttpSession session) {
        User user =(User)  session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user);
        productImageService.setFirstProdutImagesOnOrderItems(ois);
        return ois;
    }
    @GetMapping("forechangeOrderItem")
    public Object changeOrderItem( HttpSession session, int pid, int num) {
        User user =(User)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");

        List<OrderItem> ois = orderItemService.listByUser(user);
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId()==pid){
                oi.setNumber(num);
                orderItemService.update(oi);
                break;
            }
        }
        return Result.success();
    }
    @GetMapping("foredeleteOrderItem")
    public Object deleteOrderItem(HttpSession session,int oiid){
        User user =(User)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");
        orderItemService.delete(oiid);
        return Result.success();
    }
    @PostMapping("forecreateOrder")
    public Object createOrder(@RequestBody Order order,HttpSession session){
        User user =(User)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderService.waitPay);
        List<OrderItem> ois= (List<OrderItem>)  session.getAttribute("ois");

        Money total =orderService.add(order,ois);

        Map<String,Object> map = new HashMap<>();
        map.put("oid", order.getId());
        map.put("total", total);

        return Result.success(map);
    }
    @GetMapping("forepayed")
    public Object payed(int oid) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        return order;
    }
    @GetMapping("forebought")
    public Object bought(HttpSession session) {
        User user =(User)  session.getAttribute("user");
        if(null==user)
            return Result.fail("未登录");
        List<Order> os= orderService.listByUserWithoutDelete(user);
        orderService.removeOrderFromOrderItem(os);
        return os;
    }

    @GetMapping("foreconfirmPay")
    public Object confirmPay(int oid) {
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        orderService.cacl(o);
        orderService.removeOrderFromOrderItem(o);
        return o;
    }
    @GetMapping("foreorderConfirmed")
    public Object orderConfirmed( int oid) {
        Order o = orderService.get(oid);
        o.setStatus(OrderService.waitReview);
        o.setConfirmDate(new Date());
        orderService.update(o);
        return Result.success();
    }
    @PutMapping("foredeleteOrder")
    public Object deleteOrder(int oid){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.delete);
        orderService.update(o);
        return Result.success();
    }
    @GetMapping("forereview")
    public Object review(int oid) {
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        orderService.removeOrderFromOrderItem(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(p);
        productService.setSaleAndReviewNumber(p);
        Map<String,Object> map = new HashMap<>();
        map.put("p", p);
        map.put("o", o);
        map.put("reviews", reviews);

        return Result.success(map);
    }
    @PostMapping("foredoreview")
    public Object doreview( HttpSession session,int oid,int pid,String content) {
        Order o = orderService.get(oid);
        o.setStatus(OrderService.finish);
        orderService.update(o);

        Product p = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);
        User user =(User)  session.getAttribute("user");

        Review review = new Review();
        review.setContent(content);
        review.setProduct(p);
        review.setCreateDate(new Date());
        review.setUser(user);
        reviewService.add(review);
        return Result.success();
    }
}