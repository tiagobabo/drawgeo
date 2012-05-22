require 'test_helper'

class DrawsControllerTest < ActionController::TestCase
  setup do
    @draw = draws(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:draws)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create draw" do
    assert_difference('Draw.count') do
      post :create, :draw => { :challenge => @draw.challenge, :description => @draw.description, :draw => @draw.draw, :id_creator => @draw.id_creator, :latitude => @draw.latitude, :longitude => @draw.longitude, :password => @draw.password }
    end

    assert_redirected_to draw_path(assigns(:draw))
  end

  test "should show draw" do
    get :show, :id => @draw
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @draw
    assert_response :success
  end

  test "should update draw" do
    put :update, :id => @draw, :draw => { :challenge => @draw.challenge, :description => @draw.description, :draw => @draw.draw, :id_creator => @draw.id_creator, :latitude => @draw.latitude, :longitude => @draw.longitude, :password => @draw.password }
    assert_redirected_to draw_path(assigns(:draw))
  end

  test "should destroy draw" do
    assert_difference('Draw.count', -1) do
      delete :destroy, :id => @draw
    end

    assert_redirected_to draws_path
  end
end
